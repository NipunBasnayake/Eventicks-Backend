package edu.icet.eventicks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Integer eventId;
    private String name;
    private String description;
    private LocalDateTime eventDate;
    private String imageUrl;
    private String venueName;
    private String venueLocation;
    private String category;
    private Integer createdById;
    private Integer totalTickets;
    private LocalDateTime createdAt;
    private Integer availableTickets;
    private Double ticketPrice;
}
