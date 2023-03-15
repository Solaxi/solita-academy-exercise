package us.solax.bikeapp;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import us.solax.bikeapp.model.Journey;
import us.solax.bikeapp.model.Lang;
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

  private List<Station> allStations = null;

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
  public void readStationsToDatabase(String csv) {
    log.info("Reading stations CSV from " + csv);
   
    int stationCounter = 0;
    int invalidStationCounter = 0;

    Scanner scan = null;
    String[] entry = null;
    try {
      scan = new Scanner( new URL(csv).openStream());
      scan.nextLine(); // skip the first line

      while (scan.hasNextLine()) {
        entry = scan.nextLine().split(DELIMITER);

        //Validate data set count
        if (entry.length != 13) {
          invalidStationCounter++;
          continue;
        }

        //Check for duplicates
        String stationId = entry[1].trim();
        if (findStation(stationId) != null) {
          invalidStationCounter++;
          continue;
        }

        Map<Lang, String> name = new HashMap<Lang, String>();
        name.put(Lang.FI, entry[2].trim());
        name.put(Lang.SE, entry[3].trim());
        Map<Lang, String> address = new HashMap<Lang, String>();
        address.put(Lang.FI, entry[5].trim());
        address.put(Lang.SE, entry[6].trim());
        Map<Lang, String> city = new HashMap<Lang, String>();
        city.put(Lang.FI, entry[7].trim());
        city.put(Lang.SE, entry[8].trim());
        String operator = entry[9].trim();
        int capacity = Integer.parseInt(entry[10]);
        String x = entry[11].trim();
        String y = entry[12].trim();

        stationRep.save(new Station(stationId, name, address, city, operator, capacity, x, y));
        stationCounter++;
          
      }

      log.info("Added " + stationCounter + " stations, skipped " + invalidStationCounter + " stations");
    }
    catch (Exception e) {
      log.error("Failed reading csv from " + csv, e);
      log.error("Failed line: " + Arrays.toString(entry));
    } finally {
      scan.close();
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
  public void readJourneysToDatabase(String csv) {
    log.info("Reading Journeys CSV from " + csv);
   
    int journeyCounter = 0;
    int invalidJourneyCounter = 0;
    int invalidJourneyTimeCounter = 0;
    int invalidJourneyLengthCounter = 0;
    int invalidJourneyStationCounter = 0;
    int invalidJourneyDuplicateCounter = 0;

    Scanner scan = null; 
    String[] entry = null;
    try {
      scan = new Scanner(new URL(csv).openStream());
      scan.nextLine(); // skip the first line

      while (scan.hasNextLine()) {
        entry = scan.nextLine().split(DELIMITER);

         //Validate data set count
         if (entry.length != 8) {
          invalidJourneyCounter++;
          continue;
        }

        LocalDateTime departureTime = LocalDateTime.parse(entry[0], FORMATTER);
        LocalDateTime returnTime = LocalDateTime.parse(entry[1], FORMATTER);
        String departureStationId = entry[2];
        String returnStationId = entry[4];
        int distance = Double.valueOf(entry[6]).intValue();
        int duration = Integer.valueOf(entry[7]);

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

        //Validate if added already
        Journey existingJourney = findJourney(departureTime, returnTime);
        if (existingJourney != null) {
          invalidJourneyDuplicateCounter++;
          continue;
        }

        Station departureStation = findStation(departureStationId);
        Station returnStation = findStation(returnStationId);

        //Validate journey stations (both need to exist)
        if (departureStation == null || returnStation == null) {
          invalidJourneyStationCounter++;
          continue;
        }

        Journey newJourney = journeyRep.save(new Journey(departureTime, returnTime, departureStation, returnStation, distance, duration));
        addJourneyToStation(newJourney, departureStation);
        addJourneyToStation(newJourney, returnStation);

        journeyCounter++;
      }

      log.info("Added " + journeyCounter + " journeys"
        + ", invalids: " + invalidJourneyCounter
        + ", invalidTimes: " + invalidJourneyTimeCounter
        + ", invalidLengths: " + invalidJourneyLengthCounter
        + ", invalidStations: " + invalidJourneyStationCounter
        + ", duplicates: " + invalidJourneyDuplicateCounter);
    }
    catch (Exception e) {
      log.error("Failed reading csv from " + csv, e);
      log.error("Failed line: " + Arrays.toString(entry));
    } finally {
      scan.close();
    }
  }

  //Append journey document to station and save it
  private void addJourneyToStation(Journey newJourney, Station station) {
    station.appendJourney(newJourney);
    stationRep.save(station);
  }

  //Find station
  private Station findStation(String stationId) {
    if (allStations == null) {
      allStations = stationRep.findAll();
    }
    
    for (Station station : allStations) {
      if (station.getStationId().equals(stationId)) {
        return station;
      }
    }

    return null;
  }

  //Find journey
  private Journey findJourney(LocalDateTime departureTime, LocalDateTime returnTime) {
    return journeyRep.findByTimes(departureTime, returnTime);
  }
}