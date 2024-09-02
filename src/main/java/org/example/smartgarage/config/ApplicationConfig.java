package org.example.smartgarage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

public class ApplicationConfig {

    @Bean
    public RestClient restClient(){
        return RestClient.create();
    }
}
