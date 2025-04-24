package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.PaymentDto;
import edu.icet.eventicks.entity.PaymentEntity;
import edu.icet.eventicks.repository.PaymentRepository;
import edu.icet.eventicks.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        if (paymentDto == null) {
            return null;
        }
        if (paymentDto.getPaymentId() == null || !paymentRepository.existsById(paymentDto.getPaymentId())) {
            PaymentEntity saved = paymentRepository.save(modelMapper.map(paymentDto, PaymentEntity.class));
            return modelMapper.map(saved, PaymentDto.class);
        }
        return null;
    }

    @Override
    public PaymentDto getPaymentById(Integer paymentId) {
        return paymentRepository.findById(paymentId)
                .map(paymentEntity -> modelMapper.map(paymentEntity, PaymentDto.class))
                .orElse(null);  // Return null if no payment found with the given ID
    }

    @Override
    public List<PaymentDto> getPaymentsByBuyer(Integer buyerId) {
        List<PaymentEntity> payments = paymentRepository.findAll();
        return payments.stream()
                .filter(entity -> entity.getBuyerId().equals(buyerId))
                .map(paymentEntity -> modelMapper.map(paymentEntity, PaymentDto.class))
                .toList();
    }

    @Override
    public List<PaymentDto> getPaymentsByTicket(Integer ticketId) {
        List<PaymentEntity> payments = paymentRepository.findAll();
        return payments.stream()
                .filter(entity -> entity.getTicketId().equals(ticketId))
                .map(paymentEntity -> modelMapper.map(paymentEntity, PaymentDto.class))
                .toList();
    }

    @Override
    public String processStripePayment(Integer ticketId, Integer buyerId, Integer quantity, String stripeToken) {
        return "mock-payment-intent-id";
    }

    @Override
    public PaymentDto confirmStripePayment(String paymentIntentId) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentId(1);
        paymentDto.setBuyerId(1);
        paymentDto.setTicketId(1);
        paymentDto.setQuantity(2);
        paymentDto.setTotalAmount(BigDecimal.valueOf(200.00));
        paymentDto.setMethod("Stripe");
        paymentDto.setPaidAt(java.time.LocalDateTime.now());

        return paymentDto;
    }
}
