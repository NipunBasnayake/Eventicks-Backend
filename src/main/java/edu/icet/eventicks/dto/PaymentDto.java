package edu.icet.eventicks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Integer paymentId;
    private Integer buyerId;
    private String buyerUsername;
    private Integer ticketId;
    private Integer eventId;
    private String eventName;
    private Integer quantity;
    private BigDecimal totalAmount;
    private String method;
    private LocalDateTime paidAt;
}
