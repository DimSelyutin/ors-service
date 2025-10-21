package com.innowise.swimdom.controller;

import com.innowise.swimdom.openapi.model.PoolDto;
import com.innowise.swimdom.service.PoolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  Controller for pools endpoints.
 **/
@RestController
@RequestMapping("/api/v1/pools")
public class PoolController {

    private final PoolService poolService;

    public PoolController(PoolService poolService) {
        this.poolService = poolService;
    }

    @PostMapping
    public ResponseEntity<PoolDto> createPool(@RequestBody PoolDto poolDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(poolService.createPool(poolDto));
    }

    @GetMapping
    public ResponseEntity<List<PoolDto>> getPools(PoolDto filter) {
        return ResponseEntity.ok(poolService.searchPools(filter));
    }
}


