package com.innowise.swimdom.config;

import org.springframework.boot.test.context.TestConfiguration;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;

/**
 * Embedded Redis server for integration testing.
 */
@TestConfiguration
public class EmbeddedRedisTestConfiguration {

    private static RedisServer redisServer;

    @PostConstruct
    public void startRedis() {
        if (redisServer == null) {
            redisServer = new RedisServer(6379);
            redisServer.start();
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
