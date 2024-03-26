package com.example;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class NotificationRepository implements ReactivePanacheMongoRepository<Notification> {
    public Uni<Void> add(String msg) {
        Notification notification = new Notification();
        notification.setMessage(msg);
        return persist(notification).replaceWithVoid();
    }

    public Uni<Notification> get(ObjectId id) {
        return findById(id);
    }

    public Uni<List<Notification>> getAll() {
        return findAll().list();
    }

    public Uni<Void> update(ObjectId id, String msg) {
        Notification notification = (Notification) find("_id", id).firstResult();
        if(notification == null) {
            return Uni.createFrom().nullItem();
        }
        return persist(notification).replaceWithVoid();
    }

    public Uni<Void> delete(ObjectId id) {
        return deleteById(id).replaceWithVoid();
    }
}
