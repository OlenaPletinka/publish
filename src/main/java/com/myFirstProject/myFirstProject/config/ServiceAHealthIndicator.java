package com.myFirstProject.myFirstProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ServiceAHealthIndicator implements HealthIndicator {

    @Autowired
    private GracefulShutdown gracefulShutdown;

    @Override
    public Health health() {
        if (!isRunningServiceA()) {
            return Health.down().build();
        }
        return Health.up().build();
    }

    private Boolean isRunningServiceA() {
        if (!gracefulShutdown.getFlag()) {
            return true;
        } else {
            return false;
        }

    }
}