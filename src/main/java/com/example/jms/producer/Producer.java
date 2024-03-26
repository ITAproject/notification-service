package com.example.jms.producer;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import jakarta.jms.*;


@ApplicationScoped
public class Producer implements Runnable {

    @Inject
    ConnectionFactory connectionFactory;

    private final Logger log = Logger.getLogger(Producer.class.getName());


    private final Random random = new Random();
    private final String[] notifications = {"All chocolates 20% off!", "New products everyday!", "Upcoming promotion: Buy 2 get 1 free!"};
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    void onStart(@Observes StartupEvent ev) {
        scheduler.scheduleWithFixedDelay(this, 0L, 5L, TimeUnit.SECONDS);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        try (JMSContext context = connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {
            String randomNotification = notifications[random.nextInt(notifications.length)];
            context.createProducer().send(context.createQueue("notifications"), randomNotification);
            log.info("Sent message: " + randomNotification);
        }
    }
}
