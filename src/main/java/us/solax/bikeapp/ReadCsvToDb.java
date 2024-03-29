package us.solax.bikeapp;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import us.solax.bikeapp.csv.StationCsvReader;
import us.solax.bikeapp.model.Journey;
import us.solax.bikeapp.model.Station;
import us.solax.bikeapp.repository.JourneyRepository;
import us.solax.bikeapp.repository.StationRepository;

/**
 * ReadCsvToDb is a CommandLineRunner Bean to read data from CSV to database.
 * 
 * If csv.stations or csv.journeys parameters are given as parameter,
 * uses those to parse CSV file and reads the contents to database.
 * 
 * Otherwise does nothing.
 */
@SpringBootApplication
public class ReadCsvToDb implements CommandLineRunner {
  
  private static final Logger log = LoggerFactory.getLogger(ReadCsvToDb.class);
  
  private static final String DELIMITER = ",";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  @Autowired
  private JourneyRepository journeyRep;
  @Autowired
  private StationRepository stationRep;

  /*
   * Starts the ReadCsvToDb Bean as a CommandLineRunner
   * and checks if the application was started providing
   * csv.stations or csv.journeys parameters.
   */
  @Override
  public void run(String... args) throws Exception {
    System.out.println("Started ReadCsvToDb");
    String csvStations = parseArg(args, "csv.stations");
    if (csvStations != null) {
      readStationsToDatabase(csvStations);
    }
    String csvJourneys = parseArg(args, "csv.journeys");
    if (csvJourneys != null) {
      readJourneysToDatabase(csvJourneys);
    }
  }

  /*
   * Remove key and = from command line parameter.
   * csv.stations=[csv] leaves just [csv]
   */
  private static String parseArg(String[] args, String argToFind) {
    for (String arg : args) {
      if (arg.contains(argToFind)) {
        return arg.replace(argToFind + "=", "");
      }
    }
    return null;
  }

  /**
   * Reads stations from CSV, like
   * https://opendata.arcgis.com/datasets/726277c507ef4914b0aec3cbcfcbfafc_0.csv
   * 
   * Example of one entry
   * 1,501,Hanasaari,Hanaholmen,Hanasaari,Hanasaarenranta 1,Hanaholmsstranden 1,Espoo,Esbo,CityBike Finland,10,24.840319,60.16582
   *
   * @param csv URL to csv to read from
   */
  private void readStationsToDatabase(String csv) {
    log.info("Reading stations CSV from " + csv);
   
    int stationCounter = 0;
    int invalidStationCounter = 0;

    StationCsvReader reader = null;
    Station station = null;
    try {
      reader = new StationCsvReader(csv);

      while (reader.hasNext()) {
        station = reader.next();

        //Check for duplicates
        if (stationRep.findByStationId(station.getStationId()) != null) {
          invalidStationCounter++;
          continue;
        }

        stationRep.save(station);
        
        stationCounter++;
        if (stationCounter % 100 == 0) {
          log.info("Added " + stationCounter + " stations...");
        }
      }

      log.info("Added " + stationCounter + " stations, skipped " + invalidStationCounter + " stations");
    }
    catch (Exception e) {
      log.error("Failed station: " + station, e);
    } finally {
      reader.close();
    }
  }

   /**
   * Reads journeys from CSV, like
   * https://dev.hsl.fi/citybikes/od-trips-2021/2021-05.csv
   * 
   * Example of one entry
   * 2021-05-31T23:57:25,2021-06-01T00:05:46,094,Laajalahden aukio,100,Teljäntie,2043,500
   *
   * @param csv URL to csv to read from
   */
  private void readJourneysToDatabase(String csv) {
    log.info("Reading Journeys CSV from " + csv);
    
    if (!new File(csv).exists()) {
      log.error("File doesn't exist: " + csv);
      return;
    }

    int journeyCounter = 0;
    int invalidJourneyCounter = 0;
    int invalidJourneyTimeCounter = 0;
    int invalidJourneyLengthCounter = 0;
    int invalidJourneyStationCounter = 0;

    Scanner scan = null; 
    String[] entry = null;
    try {
      List<Station> allStations = stationRep.findAll();

      scan = new Scanner(new FileInputStream(new File(csv)));
      scan.nextLine(); // skip the first line

      while (scan.hasNextLine()) {
        entry = scan.nextLine().split(DELIMITER);

         //Validate data set count
         if (entry.length != 8) {
          invalidJourneyCounter++;
          continue;
        }

        LocalDateTime departureTime = getTime(entry[0]);
        LocalDateTime returnTime = getTime(entry[1]);
        if (departureTime == null || returnTime == null) {
          invalidJourneyCounter++;
          continue;
        }

        int departureStationId = getInt(entry[2]);
        int returnStationId = getInt(entry[4]);
        int distance = getInt(entry[6]);
        int duration = getInt(entry[7]);

        //Validate journey time (must be more than 10 sec)
        if (duration < 10) {
          invalidJourneyTimeCounter++;
          continue;
        }

        //Validate journey length (must be more than 10 m)
        if (distance < 10) {
          invalidJourneyLengthCounter++;
          continue;
        }

        Station departureStation = findStation(departureStationId, allStations);
        Station returnStation = findStation(returnStationId, allStations);

        //Validate journey stations (both need to exist)
        if (departureStation == null || returnStation == null) {
          invalidJourneyStationCounter++;
          continue;
        }

        journeyRep.save(new Journey(departureTime, returnTime, departureStation, returnStation, distance, duration));

        journeyCounter++;
        if (journeyCounter % 1000 == 0) {
          log.info("Added " + journeyCounter + " journeys...");
        }
      }

      log.info("Added " + journeyCounter + " journeys"
        + ", invalids: " + invalidJourneyCounter
        + ", invalidTimes: " + invalidJourneyTimeCounter
        + ", invalidLengths: " + invalidJourneyLengthCounter
        + ", invalidStations: " + invalidJourneyStationCounter);
    }
    catch (Exception e) {
      log.error("Failed reading csv from " + csv, e);
      log.error("Failed line: " + Arrays.toString(entry));
    } finally {
      scan.close();
    }
  }

  //Ignore errors for ints
  private int getInt(String arg) {
    try {
      return Integer.parseInt(arg);
    } catch (Exception e) {}
    return -1;
  }

  //Ignore errors for dates
  private LocalDateTime getTime(String arg) {
    try {
      return LocalDateTime.parse(arg, FORMATTER);
    } catch (Exception e) {}
    return null;
  }

  //Find station
  private Station findStation(int stationId, List<Station> allStations) {
    for (Station station : allStations) {
      if (station.getStationId() == stationId) {
        return station;
      }
    }
    return null;
  }
}