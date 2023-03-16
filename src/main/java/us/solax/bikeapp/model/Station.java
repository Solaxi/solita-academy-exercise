package us.solax.bikeapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {
  
  @Id
  private String id;
  @JsonProperty("ID") private int stationId;
  @JsonProperty("Nimi") private String name;
  @JsonProperty("Osoite") private String address;
  @JsonProperty("Kaupunki") private String city;
  @JsonProperty("Operaattor") private String operator;
  @JsonProperty("Kapasiteet") private int capacity;
  @JsonProperty("x") private String x;
  @JsonProperty("y") private String y;

  public Station() {}

  public Station(
    int stationId, 
    String name,
    String address,
    String city,
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

  public int getStationId() {
    return stationId;
  }

  public void setStationId(int stationId) {
    this.stationId = stationId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
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

}
