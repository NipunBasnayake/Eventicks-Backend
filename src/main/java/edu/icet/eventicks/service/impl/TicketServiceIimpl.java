package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.TicketDto;
import edu.icet.eventicks.service.TicketService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TicketServiceIimpl implements TicketService {
    @Override
    public ResponseEntity<Boolean> addTicket(TicketDto ticketDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<TicketDto>> getAllTickets() {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> updateTicket(TicketDto ticketDto) {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> deleteTicket(Long id) {
        return null;
    }
}
