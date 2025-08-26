package com.innowise.swimdom.controller;

import com.innowise.swimdom.openapi.model.PoolDto;
import com.innowise.swimdom.service.PoolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@WithMockUser
class PoolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PoolService poolService;

    @Autowired
    private ObjectMapper objectMapper;

    private PoolDto testPoolDto;
    private List<PoolDto> testPoolList;

    @BeforeEach
    void setUp() {
        testPoolDto = new PoolDto();
        testPoolDto.setId(UUID.randomUUID());
        testPoolDto.setName("Test Pool");
        testPoolDto.setDescription("Test Description");
        testPoolDto.setCapacity(4);
        testPoolDto.setLocation("Gomel");
        testPoolDto.setCreatedAt(LocalDateTime.now());
        testPoolDto.setUpdatedAt(LocalDateTime.now());
        testPoolList = Arrays.asList(testPoolDto);
    }

    @Test
    void createPool_Success() throws Exception {
        when(poolService.createPool(testPoolDto)).thenReturn(testPoolDto);

        mockMvc.perform(post("/api/v1/pools")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPoolDto)))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(testPoolDto)));
    }

    @Test
    void createPool_WithNullValues_ReturnsBadRequest() throws Exception {
        PoolDto nullPoolDto = new PoolDto();
        nullPoolDto.setId(UUID.randomUUID());
        // name is null

        mockMvc.perform(post("/api/v1/pools")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullPoolDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createPool_InvalidJson_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/pools")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
            .andExpect(status().isBadRequest());
    }
} 