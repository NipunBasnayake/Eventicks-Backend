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

    @Column(name = "ticket_id", nullable = false)
    private Integer ticketId;

    @Column(name = "event_name", length = 200)
    private String eventName;

    @Column(name = "qr_value", nullable = false, length = 255)
    private String qrValue;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "scanned_at")
    private LocalDateTime scannedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @Column(name = "image_url")
    private String imageUrl;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
        isValid = true;
    }
}
