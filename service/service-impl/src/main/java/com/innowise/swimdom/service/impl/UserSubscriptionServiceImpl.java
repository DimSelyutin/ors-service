package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.UserSubscription;
import com.innowise.swimdom.exceptions.UserSubscriptionNotFoundException;
import com.innowise.swimdom.mapper.UserSubscriptionMapper;
import com.innowise.swimdom.openapi.model.UserSubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionUpdateDTO;
import com.innowise.swimdom.repository.UserSubscriptionRepository;
import com.innowise.swimdom.service.UserSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * A service for managing user subscriptions (UserSubscription).
 * Provides methods for creating, receiving, updating, and deleting user subscriptions.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;

    private final UserSubscriptionMapper userSubscriptionMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserSubscriptionDTO> getAllUserSubscriptions(Pageable pageable) {
        log.info("getAllUserSubscriptions - start, pageable: {}", pageable);
        Page<UserSubscription> page = userSubscriptionRepository.findAll(pageable);
        List<UserSubscriptionDTO> dtoList = userSubscriptionMapper.toUserSubscriptionDtoList(page.getContent());
        log.info("getAllUserSubscriptions - end, returned {} items", dtoList.size());
        return dtoList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserSubscriptionDTO getUserSubscriptionById(UUID id) {
        UserSubscription entity = userSubscriptionRepository.findById(id)
            .orElseThrow(() -> new UserSubscriptionNotFoundException("UserSubscription not found with id " + id));
        return userSubscriptionMapper.toUserSubscriptionDto(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserSubscriptionDTO createUserSubscription(UserSubscriptionCreateDTO createDTO) {
        UserSubscription entity = userSubscriptionMapper.toUserSubscription(createDTO);
        UserSubscription saved = userSubscriptionRepository.save(entity);
        return userSubscriptionMapper.toUserSubscriptionDto(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserSubscriptionDTO updateUserSubscription(UUID id, UserSubscriptionUpdateDTO updateDTO) {
        UserSubscription entity = userSubscriptionRepository.findById(id)
            .orElseThrow(() -> new UserSubscriptionNotFoundException("UserSubscription not found with id " + id));
        userSubscriptionMapper.updateUserSubscriptionFromDto(updateDTO, entity);
        UserSubscription updated = userSubscriptionRepository.save(entity);
        return userSubscriptionMapper.toUserSubscriptionDto(updated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserSubscription(UUID id) {
        if (!userSubscriptionRepository.existsById(id)) {
            throw new UserSubscriptionNotFoundException("UserSubscription not found with id " + id);
        }
        userSubscriptionRepository.deleteById(id);
    }
}
