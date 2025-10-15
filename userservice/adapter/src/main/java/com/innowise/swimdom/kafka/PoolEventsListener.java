package com.innowise.swimdom.kafka;

import com.innowise.swimdom.event.PoolCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PoolEventsListener {

    private static final Logger log = LoggerFactory.getLogger(PoolEventsListener.class);

    @KafkaListener(topics = "${topics.pool-events:pool.events}", groupId = "userservice",
        properties = {
            "spring.json.value.default.type=com.innowise.swimdom.user.dto.event.PoolCreatedEvent",
            "spring.json.trusted.packages=com.innowise.swimdom.user.dto.event"
        })
    public void onPoolCreated(ConsumerRecord<String, PoolCreatedEvent> record) {
        PoolCreatedEvent event = record.value();
        log.info("Received PoolCreatedEvent: id={}, name={}, location={}",
            event.getPoolId(), event.getName(), event.getLocation());
        // TODO: handle reaction (e.g., caching, projection, relations)
    }
}


