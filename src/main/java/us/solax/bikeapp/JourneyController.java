package us.solax.bikeapp;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JourneyController {
  
  private final JourneyRepository repository;

  public JourneyController(JourneyRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/journeys")
  public List<Journey> all() {
    return repository.findAll();
  }

  @PostMapping("/journeys")
  public Journey newJourney(@RequestBody Journey newJourney) {
    return repository.save(newJourney);
  }
}
