package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.FraudDetectionDto;

import java.util.List;

public interface FraudDetectionService {
    FraudDetectionDto reportFraud(FraudDetectionDto fraudDetectionDto);

    FraudDetectionDto getFraudById(Integer fraudId);

    List<FraudDetectionDto> getFraudDetectionsByStatus(String status);

    List<FraudDetectionDto> getFraudsByUser(Integer userId);

    List<FraudDetectionDto> getFraudsByTicket(Integer ticketId);

    FraudDetectionDto updateFraudStatus(Integer fraudId, String status);

    Boolean analyzeTicketForFraud(Integer ticketId);

    Boolean analyzeUserForFraud(Integer userId);

    Object countFraudCases();

    Object countFraudCasesByEvent(Integer eventId);
}
