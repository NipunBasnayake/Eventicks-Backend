package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.TicketDto;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.repository.TicketRepository;
import edu.icet.eventicks.repository.UserRepository;
import edu.icet.eventicks.service.TicketService;

import java.util.ArrayList;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public TicketDto createTicket(TicketDto ticketDto) {
        if (ticketDto == null || !ticketRepository.existsById(ticketDto.getTicketId())) {
            return null;
        }
        TicketEntity ticketEntity = ticketRepository.save(modelMapper.map(ticketDto, TicketEntity.class));
        return modelMapper.map(ticketEntity, TicketDto.class);
    }

    @Override
    public TicketDto getTicketById(Integer ticketId) {
        if (ticketId == null || !ticketRepository.existsById(ticketId)) {
            return null;
        }
        return modelMapper.map(ticketRepository.findById(ticketId), TicketDto.class);
    }

    @Override
    public List<TicketDto> getFilteredTickets(String status, String type, Integer eventId) {
        if (status == null || status.isEmpty() || type == null || type.isEmpty() || eventId == null) {
            return Collections.emptyList();
        }
        List<TicketEntity> allTickets = ticketRepository.findAll();
        List<TicketDto> tickets = new ArrayList<>();
        allTickets.forEach(entity -> {
            if (entity.getStatus().equals(status) && entity.getType().equals(type) && entity.getEventId().equals(eventId)) {
                TicketDto ticketDto = modelMapper.map(entity, TicketDto.class);
                tickets.add(ticketDto);
            }
        });
        return tickets;
    }

    @Override
    public List<TicketDto> getTicketsByEvent(Integer eventId) {
        if (eventId == null){
            return Collections.emptyList();
        }
        return ticketRepository.findByEventId(eventId).stream()
                .map(entity -> modelMapper.map(entity, TicketDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDto> getTicketsBySeller(Integer sellerId) {
        if (sellerId == null){
            return Collections.emptyList();
        }
        return ticketRepository.findBySeller(userRepository.findById(sellerId)).stream()
                .map(entity -> modelMapper.map(entity, TicketDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TicketDto updateTicket(Integer ticketId, TicketDto ticketDto) {
        if (ticketId == null || ticketDto == null || !ticketRepository.existsById(ticketId) || !ticketId.equals(ticketDto.getTicketId())) {
            return null;
        }
        TicketEntity ticketEntity = ticketRepository.save(modelMapper.map(ticketDto, TicketEntity.class));
        return modelMapper.map(ticketEntity, TicketDto.class);
    }

    @Override
    public Boolean deleteTicket(Integer ticketId) {
        if (ticketId == null || !ticketRepository.existsById(ticketId)) {
            return false;
        }
        ticketRepository.deleteById(ticketId);
        return !ticketRepository.existsById(ticketId);
    }

    @Override
    public TicketDto updateTicketStatus(Integer ticketId, String status) {
        if (ticketId == null || !ticketRepository.existsById(ticketId)) {
            return null;
        }
        return ticketRepository.findById(ticketId)
                .map(ticketEntity -> {
                    ticketEntity.setStatus(status);
                    TicketEntity updatedTicketEntity = ticketRepository.save(ticketEntity);
                    return modelMapper.map(updatedTicketEntity, TicketDto.class);
                })
                .orElse(null);
    }
}