package edu.icet.eventicks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private Integer bidId;

    @Column(name = "ticket_id", nullable = false)
    private Integer ticketId;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @Column(name = "placed_at")
    private LocalDateTime placedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "is_highest_bid", nullable = false)
    private Boolean isHighestBid;

    @PrePersist
    protected void onCreate() {
        placedAt = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
    }
}
