package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.FraudDto;
import edu.icet.eventicks.service.FraudService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class FraudServiceImpl implements FraudService {
    @Override
    public ResponseEntity<Boolean> addFraud(FraudDto fraudDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<FraudDto>> getAllFrauds() {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> updateFraud(FraudDto fraudDto) {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> deleteFraud(Long id) {
        return null;
    }
}
