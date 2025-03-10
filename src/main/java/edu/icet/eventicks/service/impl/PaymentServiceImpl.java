package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.PaymentDto;
import edu.icet.eventicks.service.PaymentService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class PaymentServiceImpl implements PaymentService {


    @Override
    public ResponseEntity<Boolean> addPayment(PaymentDto paymentDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> updatePayment(PaymentDto paymentDto) {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> deletePayment(Long id) {
        return null;
    }
}
