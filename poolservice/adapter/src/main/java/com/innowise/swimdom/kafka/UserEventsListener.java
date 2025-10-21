package com.innowise.swimdom.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Consumer for users events.
 */
@Slf4j
@Component
public class UserEventsListener {

    @KafkaListener(topics = "${topics.user-events:user.events}", groupId = "poolservice")
    public void onUserEvent(ConsumerRecord<String, Map<String, Object>> record) {
        log.info("Received UserEvent key={} payload={}", record.key(), record.value());

    }
}


