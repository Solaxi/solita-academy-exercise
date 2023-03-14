package us.solax.bikeapp.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * One CSV Station entry from 
 * https://dev.hsl.fi/citybikes/od-trips-2021/2021-05.csv
 * 
 * Example of one data set:
 * 
 * Departure station id/Return station id         094
 * Departure station name/Return station name     Laajalahden aukio
 */
@Document
public class Station {
  
  @Id
  private String id;
  private String stationId;
  private String stationName;
  @DBRef
  @JsonIgnore
  private List<Journey> journeys = new ArrayList<Journey>();

  public Station() {}

  public Station(String stationId, String stationName) {
    this.stationId = stationId;
    this.stationName = stationName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStationId() {
    return stationId;
  }

  public void setStationId(String stationId) {
    this.stationId = stationId;
  }
  
  public String getStationName() {
    return stationName;
  }

  public void setStationName(String stationName) {
    this.stationName = stationName;
  }
  
  public List<Journey> getJourneys() {
    return journeys;
  }

  public void setJourneys(List<Journey> journeys) {
    this.journeys = journeys;
  }

  public void appendJourney(Journey journey) {
    journeys.add(journey);
  }
}
