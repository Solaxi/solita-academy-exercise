package us.solax.bikeapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * One CSV Station entry from 
 * https://opendata.arcgis.com/datasets/726277c507ef4914b0aec3cbcfcbfafc_0.csv
 * 
 * 
 * FID          1
 * ID           501
 * Nimi         Hanasaari
 * Namn         Hanaholmen
 * Name         Hanasaari (NOT USED)
 * Osoite       Hanasaarenranta 1
 * Adress       Hanaholmsstranden 1
 * Kaupunki     Espoo
 * Stad         Esbo
 * Operaattor   CityBike Finland
 * Kapasiteet   10
 * x            24.840319
 * y            60.16582
 */
@Document
public class Station {
  
  @Id
  private String id;
  private String stationId;
  private Map<Lang, String> name;
  private Map<Lang, String> address;
  private Map<Lang, String> city;
  private String operator;
  private int capacity;
  private String x;
  private String y;

  @DBRef
  @JsonIgnore
  private List<Journey> journeys = new ArrayList<Journey>();

  public Station() {}

  public Station(
    String stationId, 
    Map<Lang, String> name,
    Map<Lang, String> address,
    Map<Lang, String> city,
    String operator,
    int capacity,
    String x,
    String y
  ) {
    this.stationId = stationId;
    this.name = name;
    this.address = address;
    this.city = city;
    this.operator = operator;
    this.capacity = capacity;
    this.x = x;
    this.y = y;
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

  public Map<Lang, String> getName() {
    return name;
  }

  public void setName(Map<Lang, String> name) {
    this.name = name;
  }
  
  public Map<Lang, String> getAddress() {
    return address;
  }

  public void setAddress(Map<Lang, String> address) {
    this.address = address;
  }

  public Map<Lang, String> getCity() {
    return city;
  }

  public void setCity(Map<Lang, String> city) {
    this.city = city;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public String getX() {
    return x;
  }

  public void setX(String x) {
    this.x = x;
  }

  public String getY() {
    return y;
  }

  public void setY(String y) {
    this.y = y;
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
