package us.solax.bikeapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BikeAppApplication {

  @Value("${csv}")
  private static String csv;
  
	public static void main(String[] args) {
		SpringApplication.run(BikeAppApplication.class, args);

    if (csv != null) {
      new ReadCsvToDb().readCsvToDatabase(csv);
    }
	}

}
