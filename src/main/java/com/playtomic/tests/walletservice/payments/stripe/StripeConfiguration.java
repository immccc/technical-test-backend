package com.playtomic.tests.walletservice.payments.stripe;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class StripeConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        var restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new StripeRestTemplateResponseErrorHandler());
        return restTemplate;

    }
}