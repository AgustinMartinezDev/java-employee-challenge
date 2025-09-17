package com.reliaquest.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class MockServerClientConfig {

    @Bean
    public RestTemplate mockServerClient(
            RestTemplateBuilder builder,
            @Value("${mock.server.host}") String host,
            @Value("${mock.server.port}") int port) {

        String baseUrl = "http://" + host + ":" + port;
        return builder
                .rootUri(baseUrl)
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }
}
