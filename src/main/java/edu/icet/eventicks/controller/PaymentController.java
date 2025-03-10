package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.PaymentDto;
import edu.icet.eventicks.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {

    final PaymentService paymentService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addPayment (@RequestBody PaymentDto paymentDto) {
        return paymentService.addPayment(paymentDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updatePayment (@RequestBody PaymentDto paymentDto) {
        return paymentService.updatePayment(paymentDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deletePayment (@PathVariable Long id) {
        return paymentService.deletePayment(id);
    }
}
