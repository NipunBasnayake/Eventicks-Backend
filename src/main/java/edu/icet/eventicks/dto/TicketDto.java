package edu.icet.eventicks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private Integer ticketId;
    private Integer eventId;
    private String eventName;
    private Integer sellerId;
    private String sellerUsername;
    private BigDecimal price;
    private BigDecimal minBidPrice;
    private String type;
    private String status;
    private Integer qrCodeId;
    private LocalDateTime createdAt;
    private LocalDateTime eventDate;
    private String venueName;
}
