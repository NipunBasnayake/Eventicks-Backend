package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.TicketDto;
import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.entity.EventEntity;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.entity.UserEntity;
import edu.icet.eventicks.repository.TicketRepository;
import edu.icet.eventicks.service.EventService;
import edu.icet.eventicks.service.TicketService;
import edu.icet.eventicks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final EventService eventService;
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
        Optional<TicketEntity> ticketEntity = ticketRepository.findById(ticketId);
        return ticketEntity.map(entity -> modelMapper.map(entity, TicketDto.class)).orElse(null);
    }

    @Override
    public List<TicketDto> getFilteredTickets(String status, String type, Integer eventId) {
        if ((status == null || status.isEmpty()) &&
                (type == null || type.isEmpty()) &&
                eventId == null) {
            return Collections.emptyList();
        }

        List<TicketEntity> allTickets = ticketRepository.findAll();
        List<TicketDto> filteredTickets = new ArrayList<>();

        for (TicketEntity entity : allTickets) {
            boolean statusMatch = status == null || status.isEmpty() || entity.getStatus().equals(status);
            boolean typeMatch = type == null || type.isEmpty() || entity.getType().equals(type);
            boolean eventMatch = eventId == null || entity.getEventId().equals(eventId);

            if (statusMatch && typeMatch && eventMatch) {
                TicketDto ticketDto = modelMapper.map(entity, TicketDto.class);
                filteredTickets.add(ticketDto);
            }
        }

        return filteredTickets;
    }

    @Override
    public List<TicketDto> getTicketsByEvent(Integer eventId) {
        if (eventId == null) {
            return Collections.emptyList();
        }

        return ticketRepository.findByEvent(modelMapper.map(eventService.getEventById(eventId), EventEntity.class)).stream()
                .map(entity -> modelMapper.map(entity, TicketDto.class))
                .toList();
    }

    @Override
    public List<TicketDto> getTicketsBySeller(Integer sellerId) {
        if (sellerId == null) {
            return Collections.emptyList();
        }

        UserDto seller = userService.getUserById(sellerId);
        if (seller == null) {
            return Collections.emptyList();
        }

        return ticketRepository.findBySeller(modelMapper.map(seller, UserEntity.class)).stream()
                .map(entity -> modelMapper.map(entity, TicketDto.class))
                .toList();
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
        if (ticketId == null || status == null || status.isEmpty() || !ticketRepository.existsById(ticketId)) {
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