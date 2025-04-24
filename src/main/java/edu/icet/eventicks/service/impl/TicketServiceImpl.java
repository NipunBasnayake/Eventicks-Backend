package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.TicketDto;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.repository.TicketRepository;
import edu.icet.eventicks.service.TicketService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
        if (ticketDto == null) {
            return null;
        }
        TicketEntity entity = modelMapper.map(ticketDto, TicketEntity.class);
        entity = ticketRepository.save(entity);
        return modelMapper.map(entity, TicketDto.class);
    }

    @Override
    public TicketDto getTicketById(Integer ticketId) {
        Optional<TicketEntity> ticket = ticketRepository.findById(ticketId);
        return ticket.map(entity -> modelMapper.map(entity, TicketDto.class)).orElse(null);
    }

    @Override
    public List<TicketDto> getFilteredTickets(String status, String type, Integer eventId) {
        List<TicketEntity> allTickets = ticketRepository.findAll();

        return allTickets.stream()
                .filter(t -> status == null || t.getStatus().equalsIgnoreCase(status))
                .filter(t -> type == null || t.getType().equalsIgnoreCase(type))
                .filter(t -> eventId == null || t.getEventId().equals(eventId))
                .map(t -> modelMapper.map(t, TicketDto.class))
                .toList();
    }

    @Override
    public List<TicketDto> getTicketsByEvent(Integer eventId) {
        return ticketRepository.findAll().stream()
                .filter(t -> t.getEventId().equals(eventId))
                .map(t -> modelMapper.map(t, TicketDto.class))
                .toList();
    }

    @Override
    public List<TicketDto> getTicketsBySeller(Integer sellerId) {
        return ticketRepository.findAll().stream()
                .filter(t -> t.getSellerId().equals(sellerId))
                .map(t -> modelMapper.map(t, TicketDto.class))
                .toList();
    }

    @Override
    public TicketDto updateTicket(Integer ticketId, TicketDto ticketDto) {
        Optional<TicketEntity> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isPresent()) {
            TicketEntity entityToUpdate = optionalTicket.get();
            modelMapper.map(ticketDto, entityToUpdate);
            entityToUpdate.setTicketId(ticketId);
            TicketEntity updated = ticketRepository.save(entityToUpdate);
            return modelMapper.map(updated, TicketDto.class);
        }
        return null;
    }

    @Override
    public Boolean deleteTicket(Integer ticketId) {
        if (ticketRepository.existsById(ticketId)) {
            ticketRepository.deleteById(ticketId);
            return true;
        }
        return false;
    }

    @Override
    public TicketDto updateTicketStatus(Integer ticketId, String status) {
        Optional<TicketEntity> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isPresent()) {
            TicketEntity ticket = optionalTicket.get();
            ticket.setStatus(status);
            TicketEntity updated = ticketRepository.save(ticket);
            return modelMapper.map(updated, TicketDto.class);
        }
        return null;
    }

    @Override
    public Object countTickets() {
        return ticketRepository.count();
    }

    @Override
    public Long countTicketsByEvent(Integer eventId) {
        return ticketRepository.findAll().stream()
                .filter(t -> t.getEventId().equals(eventId))
                .count();
    }
}
