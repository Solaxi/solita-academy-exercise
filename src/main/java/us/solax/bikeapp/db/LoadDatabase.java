package us.solax.bikeapp.db;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
  
  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  public CommandLineRunner initDatabase(JourneyRepository repository) {
    return args -> {
      log.info("Preloading " + repository.save(new Journey(
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:Ss").parse("2021-05-31T23:57:25"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:Ss").parse("2021-06-01T00:05:46"),
        "094",
        "Laajalahden aukio",
        "100",
        "Teljäntie",
        2043,
        500
      )));
      log.info("Preloading " + repository.save(new Journey(
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:Ss").parse("2021-05-31T23:56:59"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:Ss").parse("2021-06-01T00:07:14"),
        "2",
        "Töölöntulli",
        "113",
        "Pasilan asema",
        1870,
        611
      )));
    };
  }
}