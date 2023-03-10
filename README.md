My implementation of Solita Dev Academy pre-assignment "just for the fun of it"

https://github.com/solita/dev-academy-2023-exercise

> *"But if you’re here just purely out of curiosity, feel free to snatch the idea and make your own city bike app just for the fun of it!"*

## Project initialized with

https://spring.io/guides/tutorials/rest/

https://start.spring.io/ with settings:
```
Project:      Maven
Language:     Java
Spring Boot:  3.0.4
Packaging:    War
Java:         19
Dependencies: Spring Web, Spring Data JPA, H2 Database
```

## Database

Developed with a local MonboDB instance.

Currently db setting defined in MongoConfig.java as:
```
mongodb://localhost:27017/bikeapp
```

## Running the service
```
$ mvn clean spring-boot:run
```

## Mappings

Runs at http://localhost:8080

```
GET /journeys  lists all journeys
```