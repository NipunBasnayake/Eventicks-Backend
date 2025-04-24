package edu.icet.eventicks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeDto {
    private Integer qrCodeId;
    private Integer ticketId;
    private String eventName;
    private String qrValue;
    private Boolean isValid;
    private LocalDateTime scannedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime generatedAt;
    private String imageUrl;
}
