package edu.icet.eventicks.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "qr_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qr_code_id")
    private Integer qrCodeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketEntity ticket;

    @Column(name = "qr_value", nullable = false)
    private String qrValue;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "scanned_at")
    private LocalDateTime scannedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
        isValid = true;
    }
}