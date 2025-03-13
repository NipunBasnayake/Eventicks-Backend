package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.ApiResponseDto;
import edu.icet.eventicks.dto.QrCodeDto;
import edu.icet.eventicks.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr-codes")
@RequiredArgsConstructor
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @PostMapping("/generate/{ticketId}")
    public ResponseEntity<ApiResponseDto<QrCodeDto>> generateQrCode(@PathVariable Integer ticketId) {
        QrCodeDto generatedQrCode = qrCodeService.generateQrCode(ticketId);
        return new ResponseEntity<>(ApiResponseDto.success("QR code generated successfully", generatedQrCode), HttpStatus.CREATED);
    }

    @GetMapping("/{qrCodeId}")
    public ResponseEntity<ApiResponseDto<QrCodeDto>> getQrCodeById(@PathVariable Integer qrCodeId) {
        QrCodeDto qrCode = qrCodeService.getQrCodeById(qrCodeId);
        return ResponseEntity.ok(ApiResponseDto.success(qrCode));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<ApiResponseDto<QrCodeDto>> getQrCodeByTicket(@PathVariable Integer ticketId) {
        QrCodeDto qrCode = qrCodeService.getQrCodeByTicket(ticketId);
        return ResponseEntity.ok(ApiResponseDto.success(qrCode));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponseDto<QrCodeDto>> verifyQrCode(@RequestParam String qrValue) {
        QrCodeDto verifiedQrCode = qrCodeService.verifyQrCode(qrValue);
        return ResponseEntity.ok(ApiResponseDto.success("QR code verified successfully", verifiedQrCode));
    }

    @PutMapping("/{qrCodeId}/invalidate")
    public ResponseEntity<ApiResponseDto<QrCodeDto>> invalidateQrCode(@PathVariable Integer qrCodeId) {
        QrCodeDto invalidatedQrCode = qrCodeService.invalidateQrCode(qrCodeId);
        return ResponseEntity.ok(ApiResponseDto.success("QR code invalidated successfully", invalidatedQrCode));
    }

    @PutMapping("/{qrCodeId}/scan")
    public ResponseEntity<ApiResponseDto<QrCodeDto>> scanQrCode(@PathVariable Integer qrCodeId) {
        QrCodeDto scannedQrCode = qrCodeService.scanQrCode(qrCodeId);
        return ResponseEntity.ok(ApiResponseDto.success("QR code scanned successfully", scannedQrCode));
    }
}