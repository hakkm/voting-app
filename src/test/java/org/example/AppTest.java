package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Basic smoke test for Spring Boot application startup.
 * More comprehensive integration tests are in VoteControllerTest.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "server.port=0",  // Use random port
        "spring.h2.console.enabled=false"
})
public class AppTest {

    @Test
    void applicationContextLoads() {
        // This test simply verifies that the Spring Boot application can start
        // Comprehensive API tests are in VoteControllerTest
    }
}

