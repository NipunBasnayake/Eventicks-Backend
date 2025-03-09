package edu.icet.eventicks.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QrCodeDto {
    private Integer qrCodeId;
    private Integer ticketId;
    private String qrValue;
    private boolean isValid;
    private LocalDateTime scannedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime generatedAt;
}
