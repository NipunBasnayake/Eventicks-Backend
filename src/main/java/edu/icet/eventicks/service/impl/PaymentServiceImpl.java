package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.PaymentDto;
import edu.icet.eventicks.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        return null;
    }

    @Override
    public PaymentDto getPaymentById(Integer paymentId) {
        return null;
    }

    @Override
    public List<PaymentDto> getPaymentsByBuyer(Integer buyerId) {
        return List.of();
    }

    @Override
    public List<PaymentDto> getPaymentsByTicket(Integer ticketId) {
        return List.of();
    }

    @Override
    public String processStripePayment(Integer ticketId, Integer buyerId, Integer quantity, String stripeToken) {
        return "";
    }

    @Override
    public PaymentDto confirmStripePayment(String paymentIntentId) {
        return null;
    }
}
