package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.FraudDetectionDto;
import edu.icet.eventicks.entity.FraudDetectionEntity;
import edu.icet.eventicks.repository.FraudDetectionRepository;
import edu.icet.eventicks.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FraudDetectionServiceImpl implements FraudDetectionService {
    private final FraudDetectionRepository frdRepo;
    private final ModelMapper modelMapper;

    @Override
    public FraudDetectionDto reportFraud(FraudDetectionDto fraudDetectionDto) {
        if (fraudDetectionDto == null) {
            return null;
        }
        if (fraudDetectionDto.getFraudId() == null || !frdRepo.existsById(fraudDetectionDto.getFraudId())) {
            FraudDetectionEntity saved = frdRepo.save(modelMapper.map(fraudDetectionDto, FraudDetectionEntity.class));
            return modelMapper.map(saved, FraudDetectionDto.class);
        }
        return null;
    }

    @Override
    public FraudDetectionDto getFraudById(Integer fraudId) {
        Optional<FraudDetectionEntity> fraudDetection = frdRepo.findById(fraudId);
        return fraudDetection.map(entity -> modelMapper.map(entity, FraudDetectionDto.class)).orElse(null);
    }

    @Override
    public List<FraudDetectionDto> getFraudDetectionsByStatus(String status) {
        List<FraudDetectionEntity> fraudDetections = frdRepo.findAll();
        return fraudDetections.stream()
                .filter(entity -> entity.getStatus().equals(status))
                .map(entity -> modelMapper.map(entity, FraudDetectionDto.class))
                .toList();
    }

    @Override
    public List<FraudDetectionDto> getFraudsByUser(Integer userId) {
        List<FraudDetectionEntity> fraudDetections = frdRepo.findAll();
        return fraudDetections.stream()
                .filter(entity -> entity.getUserId().equals(userId))
                .map(entity -> modelMapper.map(entity, FraudDetectionDto.class))
                .toList();
    }

    @Override
    public List<FraudDetectionDto> getFraudsByTicket(Integer ticketId) {
        List<FraudDetectionEntity> fraudDetections = frdRepo.findAll();
        return fraudDetections.stream()
                .filter(entity -> entity.getTicketId().equals(ticketId))
                .map(entity -> modelMapper.map(entity, FraudDetectionDto.class))
                .toList();
    }

    @Override
    public FraudDetectionDto updateFraudStatus(Integer fraudId, String status) {
        Optional<FraudDetectionEntity> fraudDetection = frdRepo.findById(fraudId);
        if (fraudDetection.isPresent()) {
            FraudDetectionEntity entity = fraudDetection.get();
            entity.setStatus(status);
            FraudDetectionEntity updatedEntity = frdRepo.save(entity);
            return modelMapper.map(updatedEntity, FraudDetectionDto.class);
        }
        return null;
    }

    @Override
    public Boolean analyzeTicketForFraud(Integer ticketId) {
        return ticketId % 2 == 0;
    }

    @Override
    public Boolean analyzeUserForFraud(Integer userId) {
        return userId % 2 == 0;
    }
}
