package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.ApiResponseDto;
import edu.icet.eventicks.dto.TicketDto;
import edu.icet.eventicks.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<TicketDto>> createTicket(@RequestBody TicketDto ticketDto) {
        TicketDto createdTicket = ticketService.createTicket(ticketDto);
        return new ResponseEntity<>(ApiResponseDto.success("Ticket created successfully", createdTicket), HttpStatus.CREATED);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<ApiResponseDto<TicketDto>> getTicketById(@PathVariable Integer ticketId) {
        TicketDto ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            return new ResponseEntity<>(ApiResponseDto.error("Ticket not found"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ApiResponseDto.success(ticket));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<TicketDto>>> getAllTickets(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer eventId) {
        List<TicketDto> tickets = ticketService.getFilteredTickets(status, type, eventId);
        return ResponseEntity.ok(ApiResponseDto.success(tickets));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ApiResponseDto<List<TicketDto>>> getTicketsByEvent(@PathVariable Integer eventId) {
        List<TicketDto> tickets = ticketService.getTicketsByEvent(eventId);
        return ResponseEntity.ok(ApiResponseDto.success(tickets));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponseDto<List<TicketDto>>> getTicketsBySeller(@PathVariable Integer sellerId) {
        List<TicketDto> tickets = ticketService.getTicketsBySeller(sellerId);
        return ResponseEntity.ok(ApiResponseDto.success(tickets));
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<ApiResponseDto<TicketDto>> updateTicket(
            @PathVariable Integer ticketId,
            @RequestBody TicketDto ticketDto) {
        TicketDto updatedTicket = ticketService.updateTicket(ticketId, ticketDto);
        if (updatedTicket == null) {
            return new ResponseEntity<>(ApiResponseDto.error("Ticket not found or update failed"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ApiResponseDto.success("Ticket updated successfully", updatedTicket));
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteTicket(@PathVariable Integer ticketId) {
        boolean deleted = ticketService.deleteTicket(ticketId);
        if (!deleted) {
            return new ResponseEntity<>(ApiResponseDto.error("Ticket not found or delete failed"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ApiResponseDto.success("Ticket deleted successfully", null));
    }

    @PutMapping("/{ticketId}/status")
    public ResponseEntity<ApiResponseDto<TicketDto>> updateTicketStatus(
            @PathVariable Integer ticketId,
            @RequestParam String status) {
        TicketDto updatedTicket = ticketService.updateTicketStatus(ticketId, status);
        if (updatedTicket == null) {
            return new ResponseEntity<>(ApiResponseDto.error("Ticket not found or status update failed"), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ApiResponseDto.success("Ticket status updated successfully", updatedTicket));
    }
}
