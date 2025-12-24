package com.brunocosta.taskflow;

import org.bson.Document;

import com.mongodb.client.MongoClient;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import lombok.RequiredArgsConstructor;

@Controller("/mongo")
@RequiredArgsConstructor
public class MongoPingController {

    private final MongoClient mongoClient;

     @Get("/ping")
    public String ping() {
        mongoClient
            .getDatabase("taskflow")
            .runCommand(new Document("ping", 1));

        return "MONGO_OK";
    }

}
