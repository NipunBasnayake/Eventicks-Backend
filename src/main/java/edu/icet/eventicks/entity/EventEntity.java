package edu.icet.eventicks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer eventId;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "venue_name", length = 200)
    private String venueName;

    @Column(name = "venue_location", length = 200)
    private String venueLocation;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "created_by_id", nullable = false)
    private Integer createdById;

    @Column(name = "total_tickets")
    private Integer totalTickets;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "available_tickets")
    private Integer availableTickets;

    @Column(name = "ticket_price")
    private Double ticketPrice;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
