package com.innowise.swimdom.service;

import com.innowise.swimdom.openapi.model.UserSubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionDTO;
import com.innowise.swimdom.openapi.model.UserSubscriptionUpdateDTO;

import java.util.List;
import java.util.UUID;

/**
 * User subscription management service.
 */
public interface UserSubscriptionService {

    List<UserSubscriptionDTO> getAll();

    List<UserSubscriptionDTO> getByUserId(UUID userId);

    UserSubscriptionDTO getById(UUID id);

    UserSubscriptionDTO create(UserSubscriptionCreateDTO createDTO);

    UserSubscriptionDTO update(UUID id, UserSubscriptionUpdateDTO updateDTO);

    void delete(UUID id);
}


