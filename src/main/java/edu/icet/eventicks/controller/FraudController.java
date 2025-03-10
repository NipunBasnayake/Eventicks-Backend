package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.EventDto;
import edu.icet.eventicks.dto.FraudDto;
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
    public ResponseEntity<Boolean> addFraud (@RequestBody FraudDto fraudDto) {
        return fraudService.addFraud(fraudDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FraudDto>> getAllFrauds() {
        return fraudService.getAllFrauds();
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateFraud (@RequestBody FraudDto fraudDto) {
        return fraudService.updateFraud(fraudDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteFraud (@PathVariable Long id) {
        return fraudService.deleteFraud(id);
    }
}
