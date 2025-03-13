package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.FraudDetectionDto;
import edu.icet.eventicks.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FraudDetectionServiceImpl implements FraudDetectionService {
    @Override
    public FraudDetectionDto reportFraud(FraudDetectionDto fraudDetectionDto) {
        return null;
    }

    @Override
    public FraudDetectionDto getFraudById(Integer fraudId) {
        return null;
    }

    @Override
    public List<FraudDetectionDto> getFraudDetectionsByStatus(String status) {
        return List.of();
    }

    @Override
    public List<FraudDetectionDto> getFraudsByUser(Integer userId) {
        return List.of();
    }

    @Override
    public List<FraudDetectionDto> getFraudsByTicket(Integer ticketId) {
        return List.of();
    }

    @Override
    public FraudDetectionDto updateFraudStatus(Integer fraudId, String status) {
        return null;
    }

    @Override
    public Boolean analyzeTicketForFraud(Integer ticketId) {
        return null;
    }

    @Override
    public Boolean analyzeUserForFraud(Integer userId) {
        return null;
    }
}
