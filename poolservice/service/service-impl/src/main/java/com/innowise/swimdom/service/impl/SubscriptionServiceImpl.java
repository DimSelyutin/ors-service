package com.innowise.swimdom.service.impl;

import com.innowise.swimdom.entity.Subscription;
import com.innowise.swimdom.mapper.SubscriptionMapper;
import com.innowise.swimdom.openapi.model.SubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionFilterDTO;
import com.innowise.swimdom.openapi.model.SubscriptionUpdateDTO;
import com.innowise.swimdom.repository.SubscriptionRepository;
import com.innowise.swimdom.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for entity {@link Subscription}.
 */
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public List<SubscriptionDTO> getAllSubscriptions(SubscriptionFilterDTO filter) {
        Specification<Subscription> spec = buildSpec(filter);
        List<Subscription> result = spec == null
            ? subscriptionRepository.findAll()
            : subscriptionRepository.findAll(spec);
        return subscriptionMapper.toDtos(result);
    }

    @Override
    public SubscriptionDTO getSubscriptionById(UUID id) {
        Subscription subscription = subscriptionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + id));
        return subscriptionMapper.toDto(subscription);
    }

    @Override
    public SubscriptionDTO createSubscription(SubscriptionCreateDTO createDTO) {
        Subscription entity = subscriptionMapper.toEntity(createDTO);
        entity = subscriptionRepository.save(entity);
        return subscriptionMapper.toDto(entity);
    }

    @Override
    @Transactional
    public SubscriptionDTO updateSubscription(SubscriptionUpdateDTO updateDTO) {
        if (updateDTO.getId() == null) {
            throw new IllegalArgumentException("Subscription id is required for update");
        }
        Subscription existing = subscriptionRepository.findById(updateDTO.getId())
            .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + updateDTO.getId()));
        subscriptionMapper.update(existing, updateDTO);
        existing = subscriptionRepository.save(existing);
        return subscriptionMapper.toDto(existing);
    }

    @Override
    @Transactional
    public void deleteSubscription(UUID id) {
        subscriptionRepository.deleteById(id);
    }

    private Specification<Subscription> buildSpec(SubscriptionFilterDTO filter) {
        if (filter == null) {
            return null;
        }
        Specification<Subscription> spec = Specification.where(null);
        if (filter.getName() != null) {
            spec = spec.and((root, cq, cb) -> cb.like(cb.lower(root.get("name")), "%"
                    + filter.getName().toLowerCase() + "%"));
        }
        if (filter.getDescription() != null) {
            spec = spec.and((root, cq, cb) -> cb.like(cb.lower(root.get("description")), "%"
                    + filter.getDescription().toLowerCase() + "%"));
        }
        if (filter.getDuration() != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("duration"), filter.getDuration()));
        }
        if (filter.getPrice() != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("price"), filter.getPrice()));
        }
        return spec;
    }
}


