package edu.icet.eventicks.dto;

import edu.icet.eventicks.util.TicketStatus;
import edu.icet.eventicks.util.TicketType;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketDto {
    private Integer ticketId;
    private Integer eventId;
    private Integer sellerId;
    private Double price;
    private Double minBidPrice;
    private TicketType type;
    private TicketStatus status;
    private Integer qrCodeId;
    private LocalDateTime createdAt;
}
