package us.solax.bikeapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import us.solax.bikeapp.model.Station;

@Repository
public interface StationRepository extends MongoRepository<Station, String> {
  
  @Query("{ 'stationId' : ?0, 'stationName' : ?1 }")
  Station findByIdAndName(String stationId, String stationName);
}
