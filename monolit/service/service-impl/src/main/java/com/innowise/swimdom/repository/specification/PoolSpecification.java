package com.innowise.swimdom.repository.specification;

import com.innowise.swimdom.entity.Pool;
import com.innowise.swimdom.openapi.model.PoolDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification for search pools.
 */
public class PoolSpecification {

    /**
     * Method for filters pool fields.
     *
     * @param filter PoolDto
     * @return class specification of pool
     */
    public static Specification<Pool> byFilter(PoolDto filter) {
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (filter.getId() != null) {
                p = cb.and(p, cb.equal(root.get("id"), filter.getId()));
            }
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getDescription() != null && !filter.getDescription().isEmpty()) {
                p = cb.and(p,
                    cb.like(cb.lower(root.get("description")), "%" + filter.getDescription().toLowerCase() + "%"));
            }
            if (filter.getLocation() != null && !filter.getLocation().isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("location")), "%" + filter.getLocation().toLowerCase() + "%"));
            }
            if (filter.getCapacity() != null) {
                p = cb.and(p, cb.equal(root.get("capacity"), filter.getCapacity()));
            }
            if (filter.getCreatedAt() != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAt()));
            }
            if (filter.getUpdatedAt() != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("updatedAt"), filter.getUpdatedAt()));
            }

            return p;
        };
    }
}
