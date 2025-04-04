package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.TicketDto;
import edu.icet.eventicks.entity.EventEntity;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.repository.EventRepository;
import edu.icet.eventicks.repository.TicketRepository;
import edu.icet.eventicks.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
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
        return null;
    }

    @Override
    public List<TicketDto> getFilteredTickets(String status, String type, Integer eventId) {
        return List.of();
    }

    @Override
    public List<TicketDto> getTicketsByEvent(Integer eventId) {
        return List.of();
    }

    @Override
    public List<TicketDto> getTicketsBySeller(Integer sellerId) {
        return List.of();
    }

    @Override
    public TicketDto updateTicket(Integer ticketId, TicketDto ticketDto) {
        return null;
    }

    @Override
    public Boolean deleteTicket(Integer ticketId) {
        return null;
    }

    @Override
    public TicketDto updateTicketStatus(Integer ticketId, String status) {
        return null;
    }
}