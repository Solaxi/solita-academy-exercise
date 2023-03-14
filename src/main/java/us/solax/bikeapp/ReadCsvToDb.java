package us.solax.bikeapp;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import us.solax.bikeapp.model.Journey;
import us.solax.bikeapp.model.Station;
import us.solax.bikeapp.repository.JourneyRepository;
import us.solax.bikeapp.repository.StationRepository;

public class ReadCsvToDb {
  
  private static final Logger log = LoggerFactory.getLogger(ReadCsvToDb.class);

  @Autowired
  private JourneyRepository journeyRep;
  @Autowired
  private StationRepository stationRep;

  private final String DELIMITER = ",";
  private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  public void readCsvToDatabase(String csv) {
    if (csv == null) {
        log.info("No CSV to Read");
    }

    log.info("Reading CSV from " + csv);
    journeyRep.deleteAll();
    stationRep.deleteAll();
    
    int journeyCounter = 0;

    Scanner scan = null;  
    try {
      scan = new Scanner( new URL(csv).openStream());
      scan.nextLine(); // skip the first line

      while (scan.hasNextLine()) {
        String[] entry = scan.nextLine().split(DELIMITER);

        //2021-05-31T23:57:25,2021-06-01T00:05:46,094,Laajalahden aukio,100,Teljäntie,2043,500
        LocalDateTime departureTime = LocalDateTime.parse(entry[0], FORMATTER);
        LocalDateTime returnTime = LocalDateTime.parse(entry[1], FORMATTER);
        String departureStationId = entry[2];
        String departureStationName = entry[3];
        String returnStationId = entry[4];
        String returnStationName = entry[5];
        Integer distance = Integer.valueOf(entry[6]);
        Integer duration = Integer.valueOf(entry[7]);

        Station departureStation = getOrAddStation(departureStationId, departureStationName);
        Station returnStation = getOrAddStation(returnStationId, returnStationName);

        Journey newJourney = journeyRep.save(new Journey(departureTime, returnTime, departureStation, returnStation, distance, duration));
        addJourneyToStation(newJourney, departureStation);
        addJourneyToStation(newJourney, returnStation);

        journeyCounter++;
        //break;
      }

      scan.close();
      log.info("Added " + journeyCounter + " journeys");
    }
    catch (Exception e) {
      log.error("Failed reading csv from " + csv, e);
    } finally {
      scan.close();
    }
 
    log.info("Done.");
  }

  private void addJourneyToStation(Journey newJourney, Station station) {
    station.appendJourney(newJourney);
    stationRep.save(station);
  }

  private Station getOrAddStation(String stationId, String stationName) throws Exception {
    Station station = stationRep.findByIdAndName(stationId, stationName);
    if (station == null) {
      station = stationRep.save(new Station(stationId, stationName));
    }
    return station;
  }
}