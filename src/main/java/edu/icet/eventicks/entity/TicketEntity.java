package edu.icet.eventicks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private UserEntity seller;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "min_bid_price", precision = 10, scale = 2)
    private BigDecimal minBidPrice;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "status", length = 50, nullable = false)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relationships
    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private QrCodeEntity qrCode;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Set<PaymentEntity> payments = new HashSet<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Set<BidEntity> bids = new HashSet<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Set<FraudDetectionEntity> fraudDetections = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = "AVAILABLE";
        }
    }
}