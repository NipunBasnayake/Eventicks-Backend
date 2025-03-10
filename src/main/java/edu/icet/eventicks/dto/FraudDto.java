package edu.icet.eventicks.dto;

import edu.icet.eventicks.util.FraudStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FraudDto {
    private Integer fraudId;
    private Integer userId;
    private Integer ticketId;
    private String reason;
    private FraudStatus status;
    private LocalDateTime detectedAt;
}
