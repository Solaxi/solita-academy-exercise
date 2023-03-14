package us.solax.bikeapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import us.solax.bikeapp.model.Journey;
import us.solax.bikeapp.model.Station;
import us.solax.bikeapp.repository.JourneyRepository;
import us.solax.bikeapp.repository.StationRepository;

@RestController
@RequestMapping("/api")
public class BikeAppController {
    
  @Autowired
  private StationRepository stationRepository;
  
  @Autowired
  private JourneyRepository journeyRepository;

  @GetMapping("/journeys")
  public List<Journey> allJourneys() {
    return journeyRepository.findAll();
  }

  @GetMapping("/stations")
  public List<Station> allStations() {
    return stationRepository.findAll();
  }
}
