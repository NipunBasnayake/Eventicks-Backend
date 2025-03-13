package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.QrCodeDto;
import edu.icet.eventicks.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
@CrossOrigin
public class QrCodeController {
    
    final QrCodeService qrCodeService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addQr (@RequestBody QrCodeDto qrCodeDto) {
        return qrCodeService.addQr(qrCodeDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<QrCodeDto>> getAllQrCodes() {
        return qrCodeService.getAllQrCodes();
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateQrCode (@RequestBody QrCodeDto qrCodeDto) {
        return qrCodeService.updateQrCode(qrCodeDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteQrCode (@PathVariable Long id) {
        return qrCodeService.deleteQrCode(id);
    }
}
