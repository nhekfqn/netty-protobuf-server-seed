package com.nhekfqn.sample.server;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class PingTest {

    private Client client;
    private String userId;

    @BeforeMethod
    public void setUp() throws Exception {
        client = new Client("localhost", 8080);
        client.connect();

        userId = RandomStringUtils.randomAlphanumeric(20);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void singlePing() throws Exception {
        // When
        int pongN = client.ping(userId);

        // Then
        assertEquals(pongN, 1);
    }

    @Test
    public void multiplePings() throws Exception {
        // Given
        int pongN = 0;
        int iterations = RandomUtils.nextInt(2, 10);

        // When
        for (int i = 0; i < iterations; i++) {
            pongN = client.ping(userId);
        }

        // Then
        assertEquals(pongN, iterations);
    }

    @Test
    public void multipleUsers() throws Exception {
        // Given
        int pongN1 = 0;
        int pongN2 = 0;
        int iterations1 = RandomUtils.nextInt(2, 10);
        int iterations2 = RandomUtils.nextInt(2, 10);
        String userId2 = RandomStringUtils.randomAlphanumeric(20);

        // When
        for (int i = 0; i < iterations1; i++) {
            pongN1 = client.ping(userId);
        }

        for (int i = 0; i < iterations2; i++) {
            pongN2 = client.ping(userId2);
        }

        // Then
        assertEquals(pongN1, iterations1);
        assertEquals(pongN2, iterations2);
    }

}
