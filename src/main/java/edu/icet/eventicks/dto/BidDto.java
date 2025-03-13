package edu.icet.eventicks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidDto {
    private Integer bidId;
    private Integer ticketId;
    private String eventName;
    private Integer userId;
    private String username;
    private BigDecimal amount;
    private String status;
    private LocalDateTime placedAt;
    private LocalDateTime expiresAt;
    private Boolean isHighestBid;
}
