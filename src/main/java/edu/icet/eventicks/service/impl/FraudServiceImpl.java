package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.FraudDetectionDto;
import edu.icet.eventicks.service.FraudService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class FraudServiceImpl implements FraudService {
    @Override
    public ResponseEntity<Boolean> addFraud(FraudDetectionDto fraudDetectionDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<FraudDetectionDto>> getAllFrauds() {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> updateFraud(FraudDetectionDto fraudDetectionDto) {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> deleteFraud(Long id) {
        return null;
    }
}
