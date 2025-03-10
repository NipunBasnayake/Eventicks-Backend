package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.PaymentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {

    ResponseEntity<Boolean> addPayment(PaymentDto paymentDto);

    ResponseEntity<List<PaymentDto>> getAllPayments();

    ResponseEntity<Boolean> updatePayment(PaymentDto paymentDto);

    ResponseEntity<Boolean> deletePayment(Long id);
}
