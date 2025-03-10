package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.FraudDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FraudService {

    ResponseEntity<Boolean> addFraud(FraudDto fraudDto);

    ResponseEntity<List<FraudDto>> getAllFrauds();

    ResponseEntity<Boolean> updateFraud(FraudDto fraudDto);

    ResponseEntity<Boolean> deleteFraud(Long id);
}
