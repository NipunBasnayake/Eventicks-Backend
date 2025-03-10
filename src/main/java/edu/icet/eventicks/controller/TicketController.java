package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.TicketDto;
import edu.icet.eventicks.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@CrossOrigin
public class TicketController {
    
    final TicketService ticketService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addTicket (@RequestBody TicketDto ticketDto) {
        return ticketService.addTicket(ticketDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TicketDto>> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateTicket (@RequestBody TicketDto ticketDto) {
        return ticketService.updateTicket(ticketDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteTicket (@PathVariable Long id) {
        return ticketService.deleteTicket(id);
    }
}
