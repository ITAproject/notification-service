package com.example;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheQuery;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Topic;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/notifications")
public class NotificationResource {

    private final Logger log = Logger.getLogger(NotificationResource.class.getName());

    @Inject
    NotificationRepository notificationRepository;

    @Inject
    ConnectionFactory connectionFactory;

    @ConfigProperty(name = "quarkus.mongodb.connection-string")
    String connectionString;

    @POST
    public Uni<Response> createNotification(Notification notification) {
        log.info("Creating notification with message: " + notification.message);
        return notificationRepository.persist(notification)
                .onItem().transform(n -> {
                    // Publish notification to JMS topic
                    try (JMSContext context = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {
                        context.createProducer().send(context.createQueue("notifications"), notification.message);
                        log.info("Notification published to JMS topic");
                    } catch (Exception e) {
                        log.severe(e.getMessage());
                    }
                    return Response.status(Response.Status.CREATED).build();
                });
    };

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Notification>> getAllNotifications() {
        log.info("Getting all notifications");
        return notificationRepository.listAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Notification> getNotification(@PathParam("id") String id) {
        log.info("Getting notification with ID: " + id);
        return notificationRepository.findById(new ObjectId(id));
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteNotification(@PathParam("id") String id) {
        log.info("Deleting notification with ID: " + id);
        return notificationRepository.deleteById(new ObjectId(id))
                .onItem().transform(n -> Response.status(Response.Status.NO_CONTENT).build());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> updateNotification(@PathParam("id") String id, Notification notification) {
        log.info("Updating notification with ID: " + id);
        notification.id = new ObjectId(id);
        log.info("Updating notification with ID: " + id);
        return notificationRepository.update(notification)
                .map(__ -> Response.ok().build());
    }
}


