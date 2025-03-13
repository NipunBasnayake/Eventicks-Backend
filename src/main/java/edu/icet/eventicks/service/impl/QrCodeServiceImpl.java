package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.QrCodeDto;
import edu.icet.eventicks.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {
    @Override
    public QrCodeDto generateQrCode(Integer ticketId) {
        return null;
    }

    @Override
    public QrCodeDto getQrCodeById(Integer qrCodeId) {
        return null;
    }

    @Override
    public QrCodeDto getQrCodeByTicket(Integer ticketId) {
        return null;
    }

    @Override
    public QrCodeDto verifyQrCode(String qrValue) {
        return null;
    }

    @Override
    public QrCodeDto invalidateQrCode(Integer qrCodeId) {
        return null;
    }

    @Override
    public QrCodeDto scanQrCode(Integer qrCodeId) {
        return null;
    }
}
