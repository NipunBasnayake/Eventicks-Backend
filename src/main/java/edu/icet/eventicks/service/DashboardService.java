package edu.icet.eventicks.service;

import java.util.Map;

public interface DashboardService {
    Map<String, Object> getDashboardSummary();

    Map<String, Object> getEventStatistics(Integer eventId);

    Map<String, Object> getTicketSalesStatistics(String period);

    Map<String, Object> getUserActivityStatistics();

    Map<String, Object> getFraudStatistics();

    Map<String, Object> getUserSpecificStatistics(Integer userId);
}
