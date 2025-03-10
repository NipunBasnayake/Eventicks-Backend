package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.TicketDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TicketService {
    
    ResponseEntity<Boolean> addTicket(TicketDto ticketDto);

    ResponseEntity<List<TicketDto>> getAllTickets();

    ResponseEntity<Boolean> updateTicket(TicketDto ticketDto);

    ResponseEntity<Boolean> deleteTicket(Long id);
}
