package us.solax.bikeapp;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * One CSV entry from 
 * https://dev.hsl.fi/citybikes/od-trips-2021/2021-05.csv
 * 
 * Example of one data set:
 * 
 * Departure                2021-05-31T23:57:25
 * Return                   2021-06-01T00:05:46
 * Departure station id     094
 * Departure station name   Laajalahden aukio
 * Return station id        100
 * Return station name      Teljäntie
 * Covered distance (m)     2043
 * Duration (sec.)          500
 */

@Entity
public class Journey {
  
  private @Id @GeneratedValue Long id;
  private Date departureTime;
  private Date returnTime;
  private String departureStationId;
  private String departureStationName;
  private String returnStationId;
  private String returnStationName;
  private Integer distance;
  private Integer duration;

  public Journey() {
  }

  public Journey(
    Date departureTime,
    Date returnTime,
    String departureStationId,
    String departureStationName,
    String returnStationId,
    String returnStationName,
    Integer distance,
    Integer duration
  ) {
    this.departureTime = departureTime;
    this.returnTime = returnTime;
    this.departureStationId = departureStationId;
    this.departureStationName = departureStationName;
    this.returnStationId = returnStationId;
    this.returnStationName = returnStationName;
    this.distance = distance;
    this.duration = duration;
  }

  public Date getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(Date departureTime) {
    this.departureTime = departureTime;
  }

  public Date getReturnTime() {
    return returnTime;
  }

  public void setReturnTime(Date returnTime) {
    this.returnTime = returnTime;
  }

  public String getDepartureStationId() {
    return departureStationId;
  }

  public void setDepartureStationId(String departureStationId) {
    this.departureStationId = departureStationId;
  }

  public String getDepartureStationName() {
    return departureStationName;
  }

  public void setDepartureStationName(String departureStationName) {
    this.departureStationName = departureStationName;
  }

  public String getReturnStationId() {
    return returnStationId;
  }

  public void setReturnStationId(String returnStationId) {
    this.returnStationId = returnStationId;
  }

  public String getReturnStationName() {
    return returnStationName;
  }

  public void setReturnStationName(String returnStationName) {
    this.returnStationName = returnStationName;
  }

  public Integer getDistance() {
    return distance;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }
  
  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }
}
