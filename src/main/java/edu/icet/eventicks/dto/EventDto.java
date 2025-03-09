package edu.icet.eventicks.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventDto {
    private Integer eventId;
    private String name;
    private String description;
    private LocalDateTime eventDate;
    private String venueName;
    private String venueLocation;
    private String category;
    private Integer createdBy;
    private Integer totalTickets;
    private LocalDateTime createdAt;
}
