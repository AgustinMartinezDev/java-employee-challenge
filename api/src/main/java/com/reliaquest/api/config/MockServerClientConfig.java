package com.reliaquest.api.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MockServerClient {

    @Bean
    public RestTemplate mockServerClient(
            RestTemplateBuilder builder,
            @Value("${mock.server.host}") String host,
            @Value("${mock.server.port}") int port) {

        String url = "http://" + host + ":" + port;
        return builder.baseUrl(url).build();
    }
}
