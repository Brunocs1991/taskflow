package com.brunocosta.taskflow;

import java.util.Map;

import org.testcontainers.containers.MongoDBContainer;
import io.micronaut.test.support.TestPropertyProvider;

public interface MongoTestResource extends   TestPropertyProvider {

    MongoDBContainer MONGO = new MongoDBContainer("mongo:7");

    @Override
    default Map<String, String> getProperties() {
        if (!MONGO.isRunning()) {
            MONGO.start();
        }
        return Map.of("mongodb.uri", MONGO.getReplicaSetUrl("taskflow"));
    }

}
