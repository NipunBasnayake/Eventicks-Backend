package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.QrCodeDto;
import edu.icet.eventicks.service.QrCodeService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class QrCodeServiceImpl implements QrCodeService {
    @Override
    public ResponseEntity<Boolean> addQr(QrCodeDto qrCodeDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<QrCodeDto>> getAllQrCodes() {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> updateQrCode(QrCodeDto qrCodeDto) {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> deleteQrCode(Long id) {
        return null;
    }
}
