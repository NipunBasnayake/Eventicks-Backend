package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.ApiResponseDto;
import edu.icet.eventicks.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getDashboardSummary() {
        Map<String, Object> summary = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(ApiResponseDto.success(summary));
    }

    @GetMapping("/event-stats")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getEventStats(
            @RequestParam(required = false) Integer eventId) {
        Map<String, Object> eventStats = dashboardService.getEventStatistics(eventId);
        return ResponseEntity.ok(ApiResponseDto.success(eventStats));
    }

    @GetMapping("/ticket-sales")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getTicketSalesStats(
            @RequestParam(required = false) String period) {
        Map<String, Object> salesStats = dashboardService.getTicketSalesStatistics(period);
        return ResponseEntity.ok(ApiResponseDto.success(salesStats));
    }

    @GetMapping("/user-activity")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getUserActivityStats() {
        Map<String, Object> userStats = dashboardService.getUserActivityStatistics();
        return ResponseEntity.ok(ApiResponseDto.success(userStats));
    }

    @GetMapping("/fraud-statistics")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getFraudStatistics() {
        Map<String, Object> fraudStats = dashboardService.getFraudStatistics();
        return ResponseEntity.ok(ApiResponseDto.success(fraudStats));
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getUserSpecificStats(@PathVariable Integer userId) {
        Map<String, Object> userStats = dashboardService.getUserSpecificStatistics(userId);
        return ResponseEntity.ok(ApiResponseDto.success(userStats));
    }
}