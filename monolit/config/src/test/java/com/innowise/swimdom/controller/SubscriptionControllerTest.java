package com.innowise.swimdom.controller;

import com.innowise.swimdom.openapi.model.SubscriptionCreateDTO;
import com.innowise.swimdom.openapi.model.SubscriptionDTO;
import com.innowise.swimdom.openapi.model.SubscriptionFilterDTO;
import com.innowise.swimdom.openapi.model.SubscriptionUpdateDTO;
import com.innowise.swimdom.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@WithMockUser
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionService subscriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    private SubscriptionDTO testSubscriptionDTO;
    private SubscriptionCreateDTO testSubscriptionCreateDTO;
    private SubscriptionUpdateDTO testSubscriptionUpdateDTO;
    private SubscriptionFilterDTO testSubscriptionFilterDTO;
    private List<SubscriptionDTO> testSubscriptionList;

    @BeforeEach
    void setUp() {
        UUID subscriptionId = UUID.randomUUID();
        UUID poolId = UUID.randomUUID();

        testSubscriptionDTO = new SubscriptionDTO();
        testSubscriptionDTO.setId(subscriptionId);
        testSubscriptionDTO.setName("Test Subscription");
        testSubscriptionDTO.setDescription("Test Description");
        testSubscriptionDTO.setPrice(100.0);
        testSubscriptionDTO.setCreatedAt(LocalDateTime.now());

        testSubscriptionCreateDTO = new SubscriptionCreateDTO();
        testSubscriptionCreateDTO.setName("Test Subscription");
        testSubscriptionCreateDTO.setDescription("Test Description");
        testSubscriptionCreateDTO.setPrice(100.0);

        testSubscriptionUpdateDTO = new SubscriptionUpdateDTO();
        testSubscriptionUpdateDTO.setId(subscriptionId);
        testSubscriptionUpdateDTO.setName("Updated Subscription");
        testSubscriptionUpdateDTO.setDescription("Updated Description");
        testSubscriptionUpdateDTO.setPrice(150.0);

        testSubscriptionFilterDTO = new SubscriptionFilterDTO();

        testSubscriptionList = Arrays.asList(testSubscriptionDTO);
    }

    // @Test
    // void createSubscription_Success() throws Exception {
    //     when(subscriptionService.createSubscription(any(SubscriptionCreateDTO.class))).thenReturn(testSubscriptionDTO);
    //
    //     mockMvc.perform(post("/api/v1/subscriptions/")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(testSubscriptionCreateDTO)))
    //         .andExpect(status().isCreated())
    //         .andExpect(content().json(objectMapper.writeValueAsString(testSubscriptionDTO)));
    // }

    @Test
    void createSubscription_InvalidJson_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/subscriptions/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createSubscription_WithNullValues_ReturnsBadRequest() throws Exception {
        SubscriptionCreateDTO nullSubscriptionDTO = new SubscriptionCreateDTO();

        // name is null

        mockMvc.perform(post("/api/v1/subscriptions/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullSubscriptionDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSubscription_Success() throws Exception {
        UUID subscriptionId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/subscriptions/{id}", subscriptionId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    void getAllSubscription_WithFilter_Success() throws Exception {
        when(subscriptionService.getAllSubscriptions(any(SubscriptionFilterDTO.class))).thenReturn(
            testSubscriptionList);

        mockMvc.perform(post("/api/v1/subscriptions/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSubscriptionFilterDTO)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(testSubscriptionList)));
    }

    @Test
    void getAllSubscription_EmptyList_Success() throws Exception {
        when(subscriptionService.getAllSubscriptions(any(SubscriptionFilterDTO.class))).thenReturn(Arrays.asList());

        mockMvc.perform(post("/api/v1/subscriptions/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSubscriptionFilterDTO)))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    void getAllSubscription_InvalidJson_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/subscriptions/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getSubscriptionById_Success() throws Exception {
        UUID subscriptionId = UUID.randomUUID();
        when(subscriptionService.getSubscriptionById(eq(subscriptionId))).thenReturn(testSubscriptionDTO);

        mockMvc.perform(get("/api/v1/subscriptions/{id}", subscriptionId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(testSubscriptionDTO)));
    }


    @Test
    void getSubscriptions_WithoutFilter_Success() throws Exception {
        when(subscriptionService.getAllSubscriptions(any(SubscriptionFilterDTO.class))).thenReturn(
            testSubscriptionList);

        mockMvc.perform(get("/api/v1/subscriptions")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(testSubscriptionList)));
    }

    @Test
    void getSubscriptions_EmptyList_Success() throws Exception {
        when(subscriptionService.getAllSubscriptions(any(SubscriptionFilterDTO.class))).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/subscriptions")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    void updateSubscription_Success() throws Exception {
        UUID subscriptionId = UUID.randomUUID();
        SubscriptionUpdateDTO subscriptionDTO = testSubscriptionUpdateDTO;


        mockMvc.perform(put("/api/v1/subscriptions/{id}", subscriptionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subscriptionDTO)))
            .andExpect(status().isOk());
    }

    @Test
    void updateSubscription_InvalidJson_ReturnsBadRequest() throws Exception {
        UUID subscriptionId = UUID.randomUUID();

        mockMvc.perform(put("/api/v1/subscriptions/{id}", subscriptionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
            .andExpect(status().isBadRequest());
    }
    //
    // @Test
    // void updateSubscription_WithNullValues_ReturnsBadRequest() throws Exception {
    //     UUID subscriptionId = UUID.randomUUID();
    //     SubscriptionUpdateDTO nullSubscriptionDTO = new SubscriptionUpdateDTO();
    //     nullSubscriptionDTO.setId(subscriptionId);
    //     // name is null
    //
    //     mockMvc.perform(put("/api/v1/subscriptions/{id}", subscriptionId)
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(nullSubscriptionDTO)))
    //         .andExpect(status().isBadRequest());
    // }
} 