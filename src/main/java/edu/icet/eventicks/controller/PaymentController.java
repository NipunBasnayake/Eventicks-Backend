package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.ApiResponseDto;
import edu.icet.eventicks.dto.PaymentDto;
import edu.icet.eventicks.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<PaymentDto>> createPayment(@RequestBody PaymentDto paymentDto) {
        PaymentDto createdPayment = paymentService.createPayment(paymentDto);
        if (createdPayment == null) {
            return new ResponseEntity<>(ApiResponseDto.error("Failed to process payment"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ApiResponseDto.success("Payment processed successfully", createdPayment), HttpStatus.CREATED);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponseDto<PaymentDto>> getPaymentById(@PathVariable Integer paymentId) {
        PaymentDto payment = paymentService.getPaymentById(paymentId);
        if (payment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error("Payment not found"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(payment));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<ApiResponseDto<List<PaymentDto>>> getPaymentsByBuyer(@PathVariable Integer buyerId) {
        List<PaymentDto> payments = paymentService.getPaymentsByBuyer(buyerId);
        if (payments == null || payments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error("No payments found for this buyer"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(payments));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<ApiResponseDto<List<PaymentDto>>> getPaymentsByTicket(@PathVariable Integer ticketId) {
        List<PaymentDto> payments = paymentService.getPaymentsByTicket(ticketId);
        if (payments == null || payments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error("No payments found for this ticket"));
        }
        return ResponseEntity.ok(ApiResponseDto.success(payments));
    }

    @PostMapping("/process-stripe")
    public ResponseEntity<ApiResponseDto<String>> processStripePayment(
            @RequestParam Integer ticketId,
            @RequestParam Integer buyerId,
            @RequestParam Integer quantity,
            @RequestParam String stripeToken) {
        String paymentIntentId = paymentService.processStripePayment(ticketId, buyerId, quantity, stripeToken);
        if (paymentIntentId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.error("Failed to initiate Stripe payment"));
        }
        return ResponseEntity.ok(ApiResponseDto.success("Stripe payment initiated", paymentIntentId));
    }

    @GetMapping("/verify-stripe/{paymentIntentId}")
    public ResponseEntity<ApiResponseDto<PaymentDto>> verifyStripePayment(@PathVariable String paymentIntentId) {
        PaymentDto confirmedPayment = paymentService.confirmStripePayment(paymentIntentId);
        if (confirmedPayment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error("Payment confirmation failed"));
        }
        return ResponseEntity.ok(ApiResponseDto.success("Payment confirmed", confirmedPayment));
    }
}
