package us.solax.bikeapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import us.solax.bikeapp.model.Journey;

@Repository
public interface JourneyRepository extends MongoRepository<Journey, String> {

}