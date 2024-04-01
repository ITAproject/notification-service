package com.example.jms;

import com.example.jms.consumer.Consumer;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.logging.Logger;


@Path("/jms")
public class NotificationResource {

    @Inject
    Consumer consumer;

    private final Logger log = Logger.getLogger(NotificationResource.class.getName());

    @GET
    @Path("notifications")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAllNotifications() {
        log.info("Getting all notifications.");
        return consumer.getNotifications();
    }
}

