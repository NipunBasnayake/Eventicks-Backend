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

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", length = 200)
    private String username;

    @Column(name = "ticket_id")
    private Integer ticketId;

    @Column(name = "event_name", length = 200)
    private String eventName;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "detected_at")
    private LocalDateTime detectedAt;
}
