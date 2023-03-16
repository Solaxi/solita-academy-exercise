package us.solax.bikeapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import us.solax.bikeapp.csv.StationCsvReader;
import us.solax.bikeapp.model.Station;

public class StationCsvReaderTests {
  
  @Test
  public void throwsExceptionIfFileNotFound() {
    Exception e = assertThrows(IOException.class, () -> { 
      new StationCsvReader("InvalidFile"); 
    });

    assertTrue(e.getMessage().contains("File InvalidFile was not found"));
  }

  /*
   * valid_station.csv contents: 
   * FID,ID,Nimi,Namn,Name,Osoite,Adress,Kaupunki,Stad,Operaattor,Kapasiteet,x,y
   * 1,501,Hanasaari,Hanaholmen,Hanasaari,Hanasaarenranta 1,Hanaholmsstranden 1,Espoo,Esbo,CityBike Finland,10,24.840319,60.16582
   */
  @Test
  public void readStationFromCsv() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("valid_station.csv").getFile());
    String absolutePath = file.getAbsolutePath();

    StationCsvReader reader = new StationCsvReader(absolutePath);
    assertTrue(reader.hasNext());
    
    Station station = reader.next();
    assertEquals(501, station.getStationId());
    assertEquals("Hanasaari", station.getName());
    assertEquals("Hanasaarenranta 1", station.getAddress());
    assertEquals("Espoo", station.getCity());
    assertEquals("CityBike Finland", station.getOperator());
    assertEquals(10, station.getCapacity());
    assertEquals("24.840319", station.getX());
    assertEquals("60.16582", station.getY());
  }
}
