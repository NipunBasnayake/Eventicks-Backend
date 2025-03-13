package edu.icet.eventicks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FraudDetectionDto {
    private Integer fraudId;
    private Integer userId;
    private String username;
    private Integer ticketId;
    private String eventName;
    private String reason;
    private String status;
    private LocalDateTime detectedAt;
}
