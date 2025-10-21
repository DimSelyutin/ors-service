package com.innowise.swimdom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Main class application.
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
public class PoolServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PoolServiceApplication.class, args);
    }
}


