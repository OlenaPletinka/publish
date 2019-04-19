package com.myFirstProject.myFirstProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateClient {
    //допомагає ходити на інші сайти за адресою і витягувати з них обєкти адреси
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
