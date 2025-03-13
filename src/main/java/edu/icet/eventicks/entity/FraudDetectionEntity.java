package edu.icet.eventicks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_detections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FraudDetectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fraud_id")
    private Integer fraudId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticket;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "detected_at")
    private LocalDateTime detectedAt;

    @PrePersist
    protected void onCreate() {
        detectedAt = LocalDateTime.now();
        if (status == null) {
            status = "PENDING_REVIEW";
        }
    }
}