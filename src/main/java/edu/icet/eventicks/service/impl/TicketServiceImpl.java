package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.TicketDto;
import edu.icet.eventicks.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    @Override
    public TicketDto createTicket(TicketDto ticketDto) {
        return null;
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
    public void deleteTicket(Integer ticketId) {

    }

    @Override
    public TicketDto updateTicketStatus(Integer ticketId, String status) {
        return null;
    }
}
