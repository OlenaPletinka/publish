package com.myFirstProject.myFirstProject.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import sun.applet.AppletListener;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// GracefulShutdown механіщм який дозволяє при схлопуванні ноди зробити паузу на всказаний час для завершення транзакцій
//використовуючи serviceAHealthIndicator ми встановили флаг хелс давн для того щоб лоад балансер не приймав нові запити на сервер

public class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {
    private static final Logger log = LoggerFactory.getLogger(GracefulShutdown.class);

    private static final int TIMEOUT = 20;

    private volatile Connector connector;

    @Getter
    @Setter
    private Boolean flag = false;

    @Autowired
    private ServiceAHealthIndicator serviceAHealthIndicator;

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        flag = true;
        serviceAHealthIndicator.health();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.connector.pause();
        Executor executor = this.connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                threadPoolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
                log.info(String.format("Application shut down gracefully within %d seconds", TIMEOUT));


//                if (!threadPoolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
//                    log.warn("Tomcat thread pool did not shut down gracefully within "
//                            + TIMEOUT + " seconds. Proceeding with forceful shutdown");
//
//                    threadPoolExecutor.shutdownNow();
//
//                    if (!threadPoolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
//                        log.error("Tomcat thread pool did not terminate");
//                    }
//                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}