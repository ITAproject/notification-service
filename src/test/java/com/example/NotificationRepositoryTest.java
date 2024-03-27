package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@QuarkusTest
public class NotificationRepositoryTest {

    @Inject
    NotificationRepository notificationRepository;

    @AfterEach
    public void cleanup() {
        notificationRepository.deleteAll().await().indefinitely();
    }

    @Test
    public void testAdd() {
        notificationRepository.add("Test").await().indefinitely();
        assert notificationRepository.listAll().await().indefinitely().size() == 1;
    }

    @Test
    public void testGetAll() {
        notificationRepository.add("Test").await().indefinitely();
        notificationRepository.add("Test2").await().indefinitely();
        assert notificationRepository.listAll().await().indefinitely().size() == 2;
    }

    @Test
    public void shouldNotGetById() {
        notificationRepository.add("Test").await().indefinitely();
        assert notificationRepository.get(new ObjectId()).await().indefinitely() == null;
    }

    @Test
    public void testGetAllZero() {
        assert notificationRepository.listAll().await().indefinitely().size() == 0;
    }

    @Test
    public void testDelete(){
        notificationRepository.add("Test").await().indefinitely();
        notificationRepository.add("Test2").await().indefinitely();
        notificationRepository.deleteAll().await().indefinitely();
        assert notificationRepository.listAll().await().indefinitely().size() == 0;
    }
}
