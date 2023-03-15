package us.solax.bikeapp.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * One CSV Journey entry from 
 * https://dev.hsl.fi/citybikes/od-trips-2021/2021-05.csv
 * 
 * Example of one data set:
 * 
 * Departure                2021-05-31T23:57:25
 * Return                   2021-06-01T00:05:46
 * Departure Station        ID linked to Station
 * Return Station           ID linked to Station
 * Covered distance (m)     2043
 * Duration (sec.)          500
 */

@Document
public class Journey {
  
  @Id
  private String id;
  private LocalDateTime departureTime;
  private LocalDateTime returnTime;
  @DBRef
  private Station departureStation;
  @DBRef
  private Station returnStation;
  private int distance;
  private int duration;

  public Journey() {
  }

  public Journey(
    LocalDateTime departureTime,
    LocalDateTime returnTime,
    Station departureStation,
    Station returnStation,
    int distance,
    int duration
  ) {
    this.departureTime = departureTime;
    this.returnTime = returnTime;
    this.departureStation = departureStation;
    this.returnStation = returnStation;
    this.distance = distance;
    this.duration = duration;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocalDateTime getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(LocalDateTime departureTime) {
    this.departureTime = departureTime;
  }

  public LocalDateTime getReturnTime() {
    return returnTime;
  }

  public void setReturnTime(LocalDateTime returnTime) {
    this.returnTime = returnTime;
  }

  public Station getDepartureStation() {
    return departureStation;
  }

  public void setDepartureStation(Station departureStation) {
    this.departureStation = departureStation;
  }

  public Station getReturnStation() {
    return returnStation;
  }

  public void setReturnStation(Station returnStation) {
    this.returnStation = returnStation;
  }

  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    this.distance = distance;
  }
  
  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }
}
