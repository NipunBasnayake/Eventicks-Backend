package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.PaymentDto;
import edu.icet.eventicks.entity.PaymentEntity;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.entity.UserEntity;
import edu.icet.eventicks.repository.PaymentRepository;
import edu.icet.eventicks.service.PaymentService;
import edu.icet.eventicks.service.TicketService;
import edu.icet.eventicks.service.UserService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserService userService;
    private final TicketService ticketService;
    private final ModelMapper modelMapper;

    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        if (paymentRepository.existsById(paymentDto.getPaymentId()) && paymentDto.getPaymentId() != null) {
            return null;
        }
        return modelMapper.map(paymentRepository.save(modelMapper.map(paymentDto, PaymentEntity.class)), PaymentDto.class);
    }

    @Override
    public PaymentDto getPaymentById(Integer paymentId) {
        if (paymentRepository.existsById(paymentId)) {
            return modelMapper.map(paymentRepository.findById(paymentId), PaymentDto.class);
        }
        return null;
    }

    @Override
    public List<PaymentDto> getPaymentsByBuyer(Integer buyerId) {
        if (buyerId == null) {
            return Collections.emptyList();
        }
        return paymentRepository.findByBuyer(modelMapper.map(userService.getUserById(buyerId), UserEntity.class)).stream()
                .map(payment -> modelMapper.map(payment, PaymentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getPaymentsByTicket(Integer ticketId) {
        if (ticketId == null) {
            return Collections.emptyList();
        }
        return paymentRepository.findByTicket(modelMapper.map(ticketService.getTicketById(ticketId), TicketEntity.class)).stream()
                .map(paymentDto -> modelMapper.map(paymentDto, PaymentDto.class))
                .toList();
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
