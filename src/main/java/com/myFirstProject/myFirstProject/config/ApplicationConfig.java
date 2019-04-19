package com.myFirstProject.myFirstProject.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ApplicationConfig {

    //@Bean - поклади в контекст цей обєкт з ретурн з іменем методу - тепер його можна автовайрити
    //JsonInclude.Include.NON_NULL - дозволяє на респонсі не писати поля які нал
    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder(){
        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
        jackson2ObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
        return jackson2ObjectMapperBuilder;
    }

    @Bean
    public GracefulShutdown gracefulShutdown() {
        return new GracefulShutdown();
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory(final GracefulShutdown gracefulShutdown) {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(gracefulShutdown);
        return factory;
    }
}


