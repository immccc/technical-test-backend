package com.playtomic.tests.wallet.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * This test is failing with the current implementation.
 *
 * How would you test this?
 */
@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = StripeConfiguration.class)
@ActiveProfiles(profiles = "test")
public class StripeServiceTest {

    private final URI testUri = URI.create("http://fake-uri.localhost");

    @Autowired
    private RestTemplate restTemplate;


    private StripeService service;
    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        service = new StripeService(testUri, testUri, restTemplate);
    }

    @Test
    @DisplayName("Should return an exception if charge fails")
    public void testChargeFails() {
        mockServer.expect(requestTo(testUri))
                .andExpect(method(POST))
                .andRespond(withStatus(UNPROCESSABLE_ENTITY));

        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> {
            service.charge("4242 4242 4242 4242", new BigDecimal(5));
        });

        mockServer.verify();
    }

    @Test
    @DisplayName("Should return nothing if charge is performed successfully")
    public void testChargesSuccessfully() throws StripeServiceException {
        mockServer.expect(requestTo(testUri))
                .andExpect(method(POST))
                .andRespond(withStatus(OK));

        service.charge("4242 4242 4242 4242", new BigDecimal(15));

        mockServer.verify();
    }
}
