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
        if (summary == null) {
            return ResponseEntity.ok(ApiResponseDto.error("No data available for the dashboard summary"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(summary));
    }

    @GetMapping("/event-stats")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getEventStats(@RequestParam(required = false) Integer eventId) {
        Map<String, Object> eventStats = dashboardService.getEventStatistics(eventId);
        if (eventStats == null) {
            return ResponseEntity.ok(ApiResponseDto.error("No data available for event statistics"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(eventStats));
    }

    @GetMapping("/ticket-sales")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getTicketSalesStats(@RequestParam(required = false) String period) {
        Map<String, Object> salesStats = dashboardService.getTicketSalesStatistics(period);
        if (salesStats == null) {
            return ResponseEntity.ok(ApiResponseDto.error("No data available for ticket sales statistics"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(salesStats));
    }

    @GetMapping("/user-activity")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getUserActivityStats() {
        Map<String, Object> userStats = dashboardService.getUserActivityStatistics();
        if (userStats == null) {
            return ResponseEntity.ok(ApiResponseDto.error("No data available for user activity statistics"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(userStats));
    }

    @GetMapping("/fraud-statistics")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getFraudStatistics() {
        Map<String, Object> fraudStats = dashboardService.getFraudStatistics();
        if (fraudStats == null) {
            return ResponseEntity.ok(ApiResponseDto.error("No data available for fraud statistics"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(fraudStats));
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getUserSpecificStats(@PathVariable Integer userId) {
        Map<String, Object> userStats = dashboardService.getUserSpecificStatistics(userId);
        if (userStats == null) {
            return ResponseEntity.ok(ApiResponseDto.error("No data available for user specific statistics"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(userStats));
    }
}
