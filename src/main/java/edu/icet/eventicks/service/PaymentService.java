package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);

    PaymentDto getPaymentById(Integer paymentId);

    List<PaymentDto> getPaymentsByBuyer(Integer buyerId);

    List<PaymentDto> getPaymentsByTicket(Integer ticketId);

    String processStripePayment(Integer ticketId, Integer buyerId, Integer quantity, String stripeToken);

    PaymentDto confirmStripePayment(String paymentIntentId);

    Object countPayments();

    Object countPaymentsByEvent(Integer eventId);
}
