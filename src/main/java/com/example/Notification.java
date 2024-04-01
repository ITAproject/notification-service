package com.example;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import org.bson.types.ObjectId;

public class Notification extends ReactivePanacheMongoEntity {
    public String message;

    public Notification() {
    }

    public Notification(String message) {
        this.message = message;
    }

}
