package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.Subscription;
<<<<<<< Updated upstream
import com.innowise.swimdom.exception.SubscriptionNotFoundException;
import com.innowise.swimdom.mapper.SubscriptionMapper;
import com.innowise.swimdom.openapi.model.SubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.SubscriptionFilterDTO;
import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionUpdateDTO;
import com.innowise.swimdom.repository.SubscriptionRepository;
import com.innowise.swimdom.repository.specification.SubscriptionSpecification;
import com.innowise.swimdom.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
=======
import com.innowise.swimdom.exceptions.SubscriptionNotFoundException;
import com.innowise.swimdom.mapper.SubscriptionMapper;
import com.innowise.swimdom.openapi.model.SubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionUpdateDTO;
import com.innowise.swimdom.repository.SubscriptionRepository;
import com.innowise.swimdom.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
>>>>>>> Stashed changes
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Subscription management service.
 * Provides methods for creating, receiving, updating, and deleting subscriptions.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionMapper subscriptionMapper;

    /**
     * {@inheritDoc}
     */
    @Override
<<<<<<< Updated upstream
    public List<SubscriptionDTO> getAllSubscriptions(SubscriptionFilterDTO subscriptionFilter) {
        log.debug("getAllSubscriptions - start");
        Specification<Subscription> spec = SubscriptionSpecification.byFilter(subscriptionFilter);
        List<Subscription> subscriptions = subscriptionRepository.findAll(spec);
=======
    public List<SubscriptionDTO> getAllSubscriptions() {
        log.debug("getAllSubscriptions - start");
        List<Subscription> subscriptions = subscriptionRepository.findAll();
>>>>>>> Stashed changes
        List<SubscriptionDTO> subscriptionDTOs = subscriptionMapper.toSubscriptionDTOList(subscriptions);
        log.debug("getAllSubscriptions - end, found {} subscriptions", subscriptionDTOs.size());
        return subscriptionDTOs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubscriptionDTO getSubscriptionById(UUID id) {
        log.debug("getSubscriptionById - start, id: {}", id);
        Subscription subscription = subscriptionRepository.findById(id)
<<<<<<< Updated upstream
            .orElseThrow(() -> {
                log.warn("getSubscriptionById - subscription with id:{} not found", id);
                return new SubscriptionNotFoundException("Subscription not found with id: " + id);
            });
=======
            .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found with id: " + id));
>>>>>>> Stashed changes
        SubscriptionDTO subscriptionDTO = subscriptionMapper.toSubscriptionDTO(subscription);
        log.debug("getSubscriptionById - end, subscriptionDTO: {}", subscriptionDTO);
        return subscriptionDTO;
    }

<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
    /**
     * {@inheritDoc}
     */
    @Override
    public SubscriptionDTO createSubscription(SubscriptionCreateDTO createDTO) {
        log.debug("createSubscription - start, createDTO: {}", createDTO);
<<<<<<< Updated upstream
        Subscription savedSubscription =
            subscriptionRepository.save(subscriptionMapper.toSubscriptionEntity(createDTO));
=======
        Subscription savedSubscription = subscriptionRepository.save(subscriptionMapper.toSubscriptionEntity(createDTO));
>>>>>>> Stashed changes
        SubscriptionDTO subscriptionDTO = subscriptionMapper.toSubscriptionDTO(savedSubscription);
        log.debug("createSubscription - end, subscriptionDTO: {}", subscriptionDTO);
        return subscriptionDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
<<<<<<< Updated upstream
    public SubscriptionDTO updateSubscription(SubscriptionUpdateDTO updateDTO) {
        log.debug("updateSubscription - start, id: {}, updateDTO: {}", updateDTO.getId());
        Subscription subscription = subscriptionRepository.findById(updateDTO.getId())
            .orElseThrow(() -> {
                log.warn("getSubscriptionById - subscription with id:{} not found", updateDTO.getId());
                return new SubscriptionNotFoundException("Subscription not found with id: " + updateDTO.getId());
            });
=======
    public SubscriptionDTO updateSubscription(UUID id, SubscriptionUpdateDTO updateDTO) {
        log.debug("updateSubscription - start, id: {}, updateDTO: {}", id, updateDTO);
        Subscription subscription = subscriptionRepository.findById(id)
            .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found with id: " + id));
>>>>>>> Stashed changes
        subscriptionMapper.updateSubscriptionFromDTO(updateDTO, subscription);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        SubscriptionDTO subscriptionDTO = subscriptionMapper.toSubscriptionDTO(savedSubscription);
        log.debug("updateSubscription - end, subscriptionDTO: {}", subscriptionDTO);
        return subscriptionDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSubscription(UUID id) {
        log.debug("deleteSubscription - start, id: {}", id);
        if (!subscriptionRepository.existsById(id)) {
<<<<<<< Updated upstream
            log.warn("deleteSubscription - subscription with id:{} not found", id);
=======
>>>>>>> Stashed changes
            throw new SubscriptionNotFoundException("Subscription not found with id: " + id);
        }
        subscriptionRepository.deleteById(id);
        log.debug("deleteSubscription - end, deleted id: {}", id);
    }
}
