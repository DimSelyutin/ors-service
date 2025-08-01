package com.innowise.swimdom.controller;

import com.innowise.swimdom.openapi.api.PoolsApi;
import com.innowise.swimdom.openapi.model.PoolDto;
import com.innowise.swimdom.service.PoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for authentication.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pools")
public class PoolController implements PoolsApi {

    private final PoolService poolService;

    /**
     * POST /pools : Create a new pool.
     */
    @Override
    @PostMapping()
    public ResponseEntity<PoolDto> createPool(@Valid @RequestBody PoolDto poolDto) {
        PoolDto createdPool = poolService.createPool(poolDto);
        return new ResponseEntity<>(createdPool, HttpStatus.CREATED);
    }

    /**
     * GET /pools : Retrieve all available pools.
     */
    @Override
    @GetMapping()
    public ResponseEntity<List<PoolDto>> getPools(PoolDto filter) {

        List<PoolDto> pools = poolService.searchPools(filter);
        if (pools.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pools);
    }
}
