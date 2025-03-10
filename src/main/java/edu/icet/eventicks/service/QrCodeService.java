package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.QrCodeDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QrCodeService {
    
    ResponseEntity<Boolean> addQr(QrCodeDto qrCodeDto);

    ResponseEntity<List<QrCodeDto>> getAllQrCodes();

    ResponseEntity<Boolean> updateQrCode(QrCodeDto qrCodeDto);

    ResponseEntity<Boolean> deleteQrCode(Long id);
}
