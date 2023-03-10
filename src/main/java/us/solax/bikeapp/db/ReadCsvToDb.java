package us.solax.bikeapp.db;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import us.solax.bikeapp.Journey;
import us.solax.bikeapp.repository.JourneyRepository;

@Configuration
public class LoadDatabase {
  
  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  public CommandLineRunner initDatabaseFromCsv(JourneyRepository repository) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    
    return args -> {
      log.info("Preloading " + repository.save(new Journey(
        LocalDateTime.parse("2021-05-31T23:57:25", formatter),
        LocalDateTime.parse("2021-06-01T00:05:46", formatter),
        "094",
        "Laajalahden aukio",
        "100",
        "Teljäntie",
        2043,
        500
      )));
    };
  }
}