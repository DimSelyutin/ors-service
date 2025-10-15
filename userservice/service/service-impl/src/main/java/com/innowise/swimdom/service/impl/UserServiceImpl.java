package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.User;
import com.innowise.swimdom.repository.UserRepository;
import com.innowise.swimdom.service.UserService;
import com.innowise.swimdom.event.UserCreatedEvent;
import com.innowise.swimdom.event.UserDeletedEvent;
import com.innowise.swimdom.event.UserUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${topics.user-events:user.events}")
    private String userEventsTopic;

    @Override
    @Transactional
    public User createUser(User user) {
        User saved = userRepository.save(user);
        kafkaTemplate.send(userEventsTopic, saved.getId().toString(),
            new UserCreatedEvent(saved.getId(), saved.getEmail(), saved.getRole().name()));
        return saved;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User saved = userRepository.save(user);
        kafkaTemplate.send(userEventsTopic, saved.getId().toString(),
            new UserUpdatedEvent(saved.getId(), saved.getEmail(), saved.getRole().name()));
        return saved;
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
        kafkaTemplate.send(userEventsTopic, id.toString(), new UserDeletedEvent(id));
    }
}


