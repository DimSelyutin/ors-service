package com.innowise.swimdom.repository.specification;

import com.innowise.swimdom.entity.Subscription;
import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionFilterDTO;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification for search subscriptions.
 */
@Slf4j
public class SubscriptionSpecification {

    /**
     * Method for filters Subscription fields.
     *
     * @param filter SubscriptionDTO
     * @return class specification of subscriptions
     */
    public static Specification<Subscription> byFilter(SubscriptionFilterDTO filter) {
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getDescription() != null && !filter.getDescription().isEmpty()) {
                p = cb.and(p,
                    cb.like(cb.lower(root.get("description")), "%" + filter.getDescription().toLowerCase() + "%"));
            }
            if (filter.getDuration() != null && !filter.getDuration().getValue().isEmpty()) {
                try {
                    p = cb.and(p, cb.equal(root.get("duration"),
                        SubscriptionDTO.DurationEnum.valueOf(filter.getDuration().getValue().toUpperCase())));
                } catch (IllegalArgumentException e) {
                    log.warn("Enum not match to DurationEnum! value:{}", filter.getDuration().name());
                }
            }
            if (filter.getPrice() != null) {
                p = cb.and(p, cb.equal(root.get("price"), filter.getPrice()));
            }

            return p;
        };
    }
}
