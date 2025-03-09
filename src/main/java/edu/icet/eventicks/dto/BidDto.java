package edu.icet.eventicks.dto;

import edu.icet.eventicks.util.BidStatus;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BidDto {
    private Integer bidId;
    private Integer ticketId;
    private Integer userId;
    private Double amount;
    private BidStatus status;
    private LocalDateTime placedAt;
    private LocalDateTime expiresAt;
}
