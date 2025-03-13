package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.FraudDetectionDto;
import edu.icet.eventicks.service.FraudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fraud")
@RequiredArgsConstructor
@CrossOrigin
public class FraudController {

    final FraudService fraudService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addFraud (@RequestBody FraudDetectionDto fraudDetectionDto) {
        return fraudService.addFraud(fraudDetectionDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FraudDetectionDto>> getAllFrauds() {
        return fraudService.getAllFrauds();
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateFraud (@RequestBody FraudDetectionDto fraudDetectionDto) {
        return fraudService.updateFraud(fraudDetectionDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteFraud (@PathVariable Long id) {
        return fraudService.deleteFraud(id);
    }
}
