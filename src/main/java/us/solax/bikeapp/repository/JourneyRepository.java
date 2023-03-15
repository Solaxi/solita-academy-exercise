package us.solax.bikeapp.repository;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import us.solax.bikeapp.model.Journey;

@Repository
public interface JourneyRepository extends MongoRepository<Journey, String> {

  @Query("{ 'departureTime' : ?0, 'returnTime' : ?1 }")
  Journey findByTimes(LocalDateTime departureTime, LocalDateTime returnTime);
}