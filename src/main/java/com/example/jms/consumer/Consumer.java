package com.example.jms.consumer;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import jakarta.jms.*;
import java.util.concurrent.atomic.AtomicReference;


/**
 * A bean consuming notifications from the JMS queue.
 */
@ApplicationScoped
public class Consumer implements Runnable {

    @Inject
    ConnectionFactory connectionFactory;

    private final ExecutorService scheduler = Executors.newSingleThreadExecutor();

    private List<String> notifications = new ArrayList<>();

    private final Logger log = Logger.getLogger(Consumer.class.getName());

    public List<String> getNotifications() {
        return notifications;
    }

    void onStart(@Observes StartupEvent ev) {
        scheduler.submit(this);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        try (JMSContext context = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {
            JMSConsumer consumer = context.createConsumer(context.createQueue("notifications"));
            while (true) {
                Message message = consumer.receive();
                log.info("Received message: " + message);
                if (message == null) return;
                String notification = message.getBody(String.class);
                log.info("Received message: " + notification);
                notifications.add(notification);
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
