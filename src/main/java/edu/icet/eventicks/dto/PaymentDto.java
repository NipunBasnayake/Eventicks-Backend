package edu.icet.eventicks.dto;

import edu.icet.eventicks.util.PaymentMethod;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDto {
    private Integer paymentId;
    private Integer buyerId;
    private Integer ticketId;
    private Integer quantity;
    private Double totalAmount;
    private PaymentMethod method;
    private LocalDateTime paidAt;
}
