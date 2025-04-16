package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.FraudDetectionDto;
import edu.icet.eventicks.dto.TicketDto;
import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.entity.FraudDetectionEntity;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.entity.UserEntity;
import edu.icet.eventicks.repository.FraudDetectionRepository;
import edu.icet.eventicks.service.FraudDetectionService;
import java.util.Collections;

import edu.icet.eventicks.service.TicketService;
import edu.icet.eventicks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FraudDetectionServiceImpl implements FraudDetectionService {
    private final FraudDetectionRepository frdRepo;
    private final UserService userService;
    private final TicketService ticketService;
    private final ModelMapper modelMapper;

    @Override
    public FraudDetectionDto reportFraud(FraudDetectionDto fraudDetectionDto) {
        if (fraudDetectionDto == null) {
            return null;
        }
        return modelMapper.map(frdRepo.save(modelMapper.map(fraudDetectionDto, FraudDetectionEntity.class)), FraudDetectionDto.class);
    }

    @Override
    public FraudDetectionDto getFraudById(Integer fraudId) {
        if (fraudId == null || !frdRepo.existsById(fraudId)) {
            return null;
        }
        return modelMapper.map(frdRepo.findById(fraudId), FraudDetectionDto.class);
    }

    @Override
    public List<FraudDetectionDto> getFraudDetectionsByStatus(String status) {
        if (status == null) {
            return Collections.emptyList();
        }
        return frdRepo.findByStatus(status).stream()
                .map(fraudDetectionEntity -> modelMapper.map(fraudDetectionEntity, FraudDetectionDto.class))
                .toList();
    }

    @Override
    public List<FraudDetectionDto> getFraudsByUser(Integer userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        UserDto userById = userService.getUserById(userId);
        return frdRepo.findByUser(modelMapper.map(userById, UserEntity.class)).stream()
                .map(fraudDetectionEntity -> modelMapper.map(fraudDetectionEntity, FraudDetectionDto.class))
                .toList();
    }

    @Override
    public List<FraudDetectionDto> getFraudsByTicket(Integer ticketId) {
        if (ticketId == null) {
            return Collections.emptyList();
        }
        TicketDto ticketById = ticketService.getTicketById(ticketId);
        return frdRepo.findByTicket(modelMapper.map(ticketById, TicketEntity.class)).stream()
                .map(fraudDetectionDto -> modelMapper.map(fraudDetectionDto, FraudDetectionDto.class))
                .toList();
    }

    @Override
    public FraudDetectionDto updateFraudStatus(Integer fraudId, String status) {
        if (status == null || status.isEmpty() || fraudId == null) {
            return null;
        }
        FraudDetectionDto fraudById = getFraudById(fraudId);
        fraudById.setStatus(status);
        return modelMapper.map(frdRepo.save(modelMapper.map(fraudById, FraudDetectionEntity.class)), FraudDetectionDto.class);
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
