package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@QuarkusTest
public class NotificationRepositoryTest {
    @Inject
    NotificationRepository notificationRepository;

    @BeforeEach
    void cleanBefore() {
        notificationRepository.deleteAll().await().indefinitely();
    }

    @Test
    void testCreateNotification() {
        Notification notification = new Notification("Test!");
        notificationRepository.persist(notification).await().indefinitely();
        assert notificationRepository.listAll().await().indefinitely().size() == 1;
    }

    @Test
    void testDeleteNotification() {
        Notification notification = new Notification("Test!");
        notificationRepository.persist(notification).await().indefinitely();
        notificationRepository.delete(notification).await().indefinitely();
        assert notificationRepository.listAll().await().indefinitely().size() == 0;
    }

    @Test
    void testGetNotification() {
        Notification notification = new Notification("Test!");
        notificationRepository.persist(notification).await().indefinitely();
        assert notificationRepository.findById(notification.id).await().indefinitely().message.equals("Test!");
    }

    @Test
    void testUpdateNotification() {
        Notification notification = new Notification("Test!");
        notificationRepository.persist(notification).await().indefinitely();
        notification.message = "Test2!";
        notificationRepository.update(notification).await().indefinitely();
        assert notificationRepository.findById(notification.id).await().indefinitely().message.equals("Test2!");
    }


}
