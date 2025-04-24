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
        if (generatedQrCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("Failed to generate QR code. Invalid ticket ID."));
        }
        return new ResponseEntity<>(ApiResponseDto.success("QR code generated successfully", generatedQrCode), HttpStatus.CREATED);
    }

    @GetMapping("/{qrCodeId}")
    public ResponseEntity<ApiResponseDto<QrCodeDto>> getQrCodeById(@PathVariable Integer qrCodeId) {
        QrCodeDto qrCode = qrCodeService.getQrCodeById(qrCodeId);
        if (qrCode == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error("QR code not found."));
        }
        return ResponseEntity.ok(ApiResponseDto.success(qrCode));
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<ApiResponseDto<QrCodeDto>> getQrCodeByTicket(@PathVariable Integer ticketId) {
        QrCodeDto qrCode = qrCodeService.getQrCodeByTicket(ticketId);
        if (qrCode == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error("No QR code found for the provided ticket."));
        }
        return ResponseEntity.ok(ApiResponseDto.success(qrCode));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponseDto<QrCodeDto>> verifyQrCode(@RequestParam String qrValue) {
        QrCodeDto verifiedQrCode = qrCodeService.verifyQrCode(qrValue);
        if (verifiedQrCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("QR code verification failed. Invalid QR code value."));
        }
        return ResponseEntity.ok(ApiResponseDto.success("QR code verified successfully", verifiedQrCode));
    }

    @PutMapping("/{qrCodeId}/invalidate")
    public ResponseEntity<ApiResponseDto<QrCodeDto>> invalidateQrCode(@PathVariable Integer qrCodeId) {
        QrCodeDto invalidatedQrCode = qrCodeService.invalidateQrCode(qrCodeId);
        if (invalidatedQrCode == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDto.error("QR code not found for invalidation."));
        }
        return ResponseEntity.ok(ApiResponseDto.success("QR code invalidated successfully", invalidatedQrCode));
    }

    @PutMapping("/{qrCodeId}/scan")
    public ResponseEntity<ApiResponseDto<QrCodeDto>> scanQrCode(@PathVariable Integer qrCodeId) {
        QrCodeDto scannedQrCode = qrCodeService.scanQrCode(qrCodeId);
        if (scannedQrCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDto.error("QR code scanning failed. Invalid QR code ID."));
        }
        return ResponseEntity.ok(ApiResponseDto.success("QR code scanned successfully", scannedQrCode));
    }
}
