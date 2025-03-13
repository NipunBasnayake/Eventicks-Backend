package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.FraudDetectionDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FraudService {

    ResponseEntity<Boolean> addFraud(FraudDetectionDto fraudDetectionDto);

    ResponseEntity<List<FraudDetectionDto>> getAllFrauds();

    ResponseEntity<Boolean> updateFraud(FraudDetectionDto fraudDetectionDto);

    ResponseEntity<Boolean> deleteFraud(Long id);
}
