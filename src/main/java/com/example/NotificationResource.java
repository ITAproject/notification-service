package com.example;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/notification")
public class NotificationResource {

    private final Logger log = Logger.getLogger(NotificationResource.class.getName());

    @Inject
    NotificationRepository notificationRepository;

    @POST
    public Uni<Void> createNotification(String message) {
        log.info("Creating notification with message: " + message);
        return notificationRepository.add(message);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAllNotifications() {
        log.info("Getting all notifications");
        return notificationRepository.listAll().await().indefinitely().stream().map(Notification::getMessage).collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Notification getNotification(@PathParam("id") String id) {
        log.info("Getting notification with ID: " + id);
        return notificationRepository.get(new ObjectId(id)).await().indefinitely();
    }

    @PUT
    @Path("/{id}")
    public Uni<Void> updateNotification(@PathParam("id") String id, String message) {
        log.info("Updating notification with ID: " + id);
        return notificationRepository.update(new ObjectId(id), message);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public void deleteNotification(@PathParam("id") String id) {
        log.info("Deleting notification with ID: " + id);
        notificationRepository.delete(new ObjectId(id)).await().indefinitely();
    }

}
