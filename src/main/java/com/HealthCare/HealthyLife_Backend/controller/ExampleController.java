package com.HealthCare.HealthyLife_Backend.controller;

import com.HealthCare.HealthyLife_Backend.dto.RankingDto;
import com.HealthCare.HealthyLife_Backend.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class ExampleController implements CrudControllerInterface<RankingDto, Long> {
    private final RankingService rankingService;

    public ExampleController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @Override
    public ResponseEntity<List<RankingDto>> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<RankingDto> findById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<RankingDto> update(Long id, RankingDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<RankingDto> create(RankingDto dto) {
        return null;
    }
}