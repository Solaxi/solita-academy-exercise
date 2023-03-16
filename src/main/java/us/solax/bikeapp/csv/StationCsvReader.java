package us.solax.bikeapp.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import us.solax.bikeapp.model.Station;

/**
 * FID,ID,Nimi,Namn,Name,Osoite,Adress,Kaupunki,Stad,Operaattor,Kapasiteet,x,y
 * 1,501,Hanasaari,Hanaholmen,Hanasaari,Hanasaarenranta 1,Hanaholmsstranden 1,Espoo,Esbo,CityBike Finland,10,24.840319,60.16582
 */
public class StationCsvReader {
  private String csvFile;
  private MappingIterator<Station> stationIter;

  /**
   * Construct a StationCsvReader with a PATH to csv file.
   * @param csvFile PATH to file
   * @throws FileNotFoundException if fileName does not exist
   */
  public StationCsvReader(final String csvFile) throws FileNotFoundException {
    this.csvFile = csvFile;
    if (!new File(csvFile).exists()) {
      throw new FileNotFoundException("File " + csvFile + " was not found");
    }
  }

  private void init() throws IOException {
    if (stationIter == null) {
      CsvMapper mapper = new CsvMapper().enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);
      CsvSchema schema = mapper
        .schemaFor(Station.class)
        .withHeader()
        .withColumnSeparator(',')
        .withColumnReordering(true);

      stationIter = mapper
        .readerFor(Station.class)
        .with(schema)
        .readValues(new FileReader(csvFile));
    }
  }

  public boolean hasNext() throws IOException {
    init();
    return stationIter.hasNext();
  }

  public Station next() throws IOException {
    init();
    return stationIter.next();
  }

  public void close() throws IOException {
    stationIter.close();
  }
}
