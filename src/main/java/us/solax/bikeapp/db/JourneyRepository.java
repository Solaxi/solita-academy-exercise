package us.solax.bikeapp.db;

import org.springframework.data.jpa.repository.JpaRepository;

import us.solax.bikeapp.Journey;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

}