package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.ApiResponseDto;
import edu.icet.eventicks.dto.FraudDetectionDto;
import edu.icet.eventicks.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fraud-detection")
@RequiredArgsConstructor
public class FraudDetectionController {

    private final FraudDetectionService fraudDetectionService;

    @PostMapping("/report")
    public ResponseEntity<ApiResponseDto<FraudDetectionDto>> reportFraud(@RequestBody FraudDetectionDto fraudDetectionDto) {
        FraudDetectionDto reportedFraud = fraudDetectionService.reportFraud(fraudDetectionDto);
        return new ResponseEntity<>(ApiResponseDto.success("Fraud reported successfully", reportedFraud), HttpStatus.CREATED);
    }

    @GetMapping("/{fraudId}")
    public ResponseEntity<ApiResponseDto<FraudDetectionDto>> getFraudById(@PathVariable Integer fraudId) {
        FraudDetectionDto fraudDetection = fraudDetectionService.getFraudById(fraudId);
        return ResponseEntity.ok(ApiResponseDto.success(fraudDetection));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<FraudDetectionDto>>> getAllFraudDetections(
            @RequestParam(required = false) String status) {
        List<FraudDetectionDto> fraudDetections = fraudDetectionService.getFraudDetectionsByStatus(status);
        return ResponseEntity.ok(ApiResponseDto.success(fraudDetections));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDto<List<FraudDetectionDto>>> getFraudsByUser(@PathVariable Integer userId) {
        List<FraudDetectionDto> fraudDetections = fraudDetectionService.getFraudsByUser(userId);
        return ResponseEntity.ok(ApiResponseDto.success(fraudDetections));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<ApiResponseDto<List<FraudDetectionDto>>> getFraudsByTicket(@PathVariable Integer ticketId) {
        List<FraudDetectionDto> fraudDetections = fraudDetectionService.getFraudsByTicket(ticketId);
        return ResponseEntity.ok(ApiResponseDto.success(fraudDetections));
    }

    @PutMapping("/{fraudId}/status")
    public ResponseEntity<ApiResponseDto<FraudDetectionDto>> updateFraudStatus(
            @PathVariable Integer fraudId,
            @RequestParam String status) {
        FraudDetectionDto updatedFraud = fraudDetectionService.updateFraudStatus(fraudId, status);
        return ResponseEntity.ok(ApiResponseDto.success("Fraud status updated successfully", updatedFraud));
    }

    @PostMapping("/analyze-ticket/{ticketId}")
    public ResponseEntity<ApiResponseDto<Boolean>> analyzeTicketForFraud(@PathVariable Integer ticketId) {
        Boolean isFraudulent = fraudDetectionService.analyzeTicketForFraud(ticketId);
        return ResponseEntity.ok(ApiResponseDto.success("Ticket analyzed for fraud", isFraudulent));
    }

    @PostMapping("/analyze-user/{userId}")
    public ResponseEntity<ApiResponseDto<Boolean>> analyzeUserForFraud(@PathVariable Integer userId) {
        Boolean isSuspicious = fraudDetectionService.analyzeUserForFraud(userId);
        return ResponseEntity.ok(ApiResponseDto.success("User analyzed for fraud", isSuspicious));
    }
}
