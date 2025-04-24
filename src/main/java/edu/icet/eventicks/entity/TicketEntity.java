package edu.icet.eventicks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer ticketId;

    @Column(name = "event_id")
    private Integer eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "seller_id")
    private Integer sellerId;

    @Column(name = "seller_username")
    private String sellerUsername;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "min_bid_price")
    private BigDecimal minBidPrice;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "qr_code_id")
    private Integer qrCodeId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "venue_name")
    private String venueName;
}
