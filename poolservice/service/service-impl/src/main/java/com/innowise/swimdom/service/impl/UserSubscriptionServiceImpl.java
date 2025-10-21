package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.mapper.UserSubscriptionMapper;
import com.innowise.swimdom.openapi.model.UserSubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionUpdateDTO;
import com.innowise.swimdom.repository.UserSubscriptionRepository;
import com.innowise.swimdom.service.UserSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for entity {@link UserSubscription}.
 */
@Service
@RequiredArgsConstructor
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserSubscriptionMapper userSubscriptionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserSubscriptionDTO> getAll() {
        return userSubscriptionMapper.toDtos(userSubscriptionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSubscriptionDTO> getByUserId(UUID userId) {
        return userSubscriptionMapper.toDtos(userSubscriptionRepository.findByUserId(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public UserSubscriptionDTO getById(UUID id) {
        UserSubscription entity = userSubscriptionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("UserSubscription not found: " + id));
        return userSubscriptionMapper.toDto(entity);
    }

    @Override
    @Transactional
    public UserSubscriptionDTO create(UserSubscriptionCreateDTO createDTO) {
        UserSubscription entity = userSubscriptionMapper.toEntity(createDTO);
        entity = userSubscriptionRepository.save(entity);
        return userSubscriptionMapper.toDto(entity);
    }

    @Override
    @Transactional
    public UserSubscriptionDTO update(UUID id, UserSubscriptionUpdateDTO updateDTO) {
        UserSubscription entity = userSubscriptionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("UserSubscription not found: " + id));
        userSubscriptionMapper.update(entity, updateDTO);
        entity = userSubscriptionRepository.save(entity);
        return userSubscriptionMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        userSubscriptionRepository.deleteById(id);
    }
}


