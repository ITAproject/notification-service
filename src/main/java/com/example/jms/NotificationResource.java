package com.example.jms;

import com.example.jms.consumer.Consumer;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.logging.Logger;


@Path("/notifications")
public class NotificationResource {

    @Inject
    Consumer consumer;

    private final Logger log = Logger.getLogger(NotificationResource.class.getName());

    @GET
    @Path("last")
    @Produces(MediaType.TEXT_PLAIN)
    public String last() {
        log.info("Getting last.");
        return consumer.getLastNotification();
    }
}

