package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.TicketDto;

import java.util.List;

public interface TicketService {
    TicketDto createTicket(TicketDto ticketDto);

    TicketDto getTicketById(Integer ticketId);

    List<TicketDto> getFilteredTickets(String status, String type, Integer eventId);

    List<TicketDto> getTicketsByEvent(Integer eventId);

    List<TicketDto> getTicketsBySeller(Integer sellerId);

    TicketDto updateTicket(Integer ticketId, TicketDto ticketDto);

    Boolean deleteTicket(Integer ticketId);

    TicketDto updateTicketStatus(Integer ticketId, String status);
}
