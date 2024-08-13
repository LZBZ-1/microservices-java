package com.lzbz.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${client.service.url}")
    private String clientServiceUrl;

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Value("${webclient.timeout}")
    private int timeout;

    @Bean
    public WebClient clientServiceWebClient() {
        return createWebClient(clientServiceUrl);
    }

    @Bean
    public WebClient productServiceWebClient() {
        return createWebClient(productServiceUrl);
    }

    private WebClient createWebClient(String baseUrl) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(timeout));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
