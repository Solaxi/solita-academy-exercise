package us.solax.bikeapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import us.solax.bikeapp.Journey;

public interface JourneyRepository extends MongoRepository<Journey, String> {

}