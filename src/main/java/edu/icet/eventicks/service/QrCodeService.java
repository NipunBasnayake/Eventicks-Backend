package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.QrCodeDto;

public interface QrCodeService {
    QrCodeDto generateQrCode(Integer ticketId);

    QrCodeDto getQrCodeById(Integer qrCodeId);

    QrCodeDto getQrCodeByTicket(Integer ticketId);

    QrCodeDto verifyQrCode(Integer ticketId);

    QrCodeDto invalidateQrCode(Integer qrCodeId);

    Object countQrCodes();
}
