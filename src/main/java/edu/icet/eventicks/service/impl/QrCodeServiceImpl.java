package edu.icet.eventicks.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import edu.icet.eventicks.dto.QrCodeDto;
import edu.icet.eventicks.entity.QrCodeEntity;
import edu.icet.eventicks.entity.TicketEntity;
import edu.icet.eventicks.repository.QrCodeRepository;
import edu.icet.eventicks.repository.TicketRepository;
import edu.icet.eventicks.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {
    private final QrCodeRepository qrCodeRepository;
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;

    @Override
    public QrCodeDto generateQrCode(Integer ticketId) {
        if (ticketId == null) {
            throw new IllegalArgumentException("Ticket ID cannot be null");
        }

        Optional<QrCodeEntity> existingQrCode = qrCodeRepository.findByTicketTicketId(ticketId);
        if (existingQrCode.isPresent()) {
            return modelMapper.map(existingQrCode.get(), QrCodeDto.class);
        }

        TicketEntity ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with ID: " + ticketId));

        String qrContent = UUID.randomUUID().toString();

        QrCodeEntity qrCodeEntity = new QrCodeEntity();
        qrCodeEntity.setTicket(ticket);
        qrCodeEntity.setQrValue(qrContent);
        qrCodeEntity.setIsValid(true);
        qrCodeEntity.setGeneratedAt(LocalDateTime.now());

        if (ticket.getEvent() != null && ticket.getEvent().getEventDate() != null) {
            qrCodeEntity.setExpiresAt(ticket.getEvent().getEventDate().plusDays(1));
        } else {
            qrCodeEntity.setExpiresAt(LocalDateTime.now().plusDays(30));
        }

        qrCodeEntity = qrCodeRepository.save(qrCodeEntity);

        QrCodeDto qrCodeDto = modelMapper.map(qrCodeEntity, QrCodeDto.class);
        qrCodeDto.setTicketId(ticket.getTicketId());

        if (ticket.getEvent() != null) {
            qrCodeDto.setEventName(ticket.getEvent().getName());
        }

        return qrCodeDto;
    }

    @Override
    public QrCodeDto getQrCodeById(Integer qrCodeId) {
        QrCodeEntity qrCodeEntity = qrCodeRepository.findById(qrCodeId)
                .orElseThrow(() -> new IllegalArgumentException("QR Code not found with ID: " + qrCodeId));

        QrCodeDto qrCodeDto = modelMapper.map(qrCodeEntity, QrCodeDto.class);
        qrCodeDto.setTicketId(qrCodeEntity.getTicket().getTicketId());

        if (qrCodeEntity.getTicket().getEvent() != null) {
            qrCodeDto.setEventName(qrCodeEntity.getTicket().getEvent().getName());
        }

        return qrCodeDto;
    }

    @Override
    public QrCodeDto getQrCodeByTicket(Integer ticketId) {
        QrCodeEntity qrCodeEntity = qrCodeRepository.findByTicketTicketId(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("QR Code not found for ticket ID: " + ticketId));

        QrCodeDto qrCodeDto = modelMapper.map(qrCodeEntity, QrCodeDto.class);
        qrCodeDto.setTicketId(qrCodeEntity.getTicket().getTicketId());

        if (qrCodeEntity.getTicket().getEvent() != null) {
            qrCodeDto.setEventName(qrCodeEntity.getTicket().getEvent().getName());
        }

        return qrCodeDto;
    }

    @Override
    public QrCodeDto verifyQrCode(String qrValue) {
        QrCodeEntity qrCodeEntity = qrCodeRepository.findByQrValue(qrValue)
                .orElseThrow(() -> new IllegalArgumentException("QR Code not found with value: " + qrValue));

        if (!qrCodeEntity.getIsValid()) {
            throw new IllegalStateException("QR Code is not valid");
        }

        if (qrCodeEntity.getExpiresAt() != null && qrCodeEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            qrCodeEntity.setIsValid(false);
            qrCodeRepository.save(qrCodeEntity);
            throw new IllegalStateException("QR Code has expired");
        }

        QrCodeDto qrCodeDto = modelMapper.map(qrCodeEntity, QrCodeDto.class);
        qrCodeDto.setTicketId(qrCodeEntity.getTicket().getTicketId());

        if (qrCodeEntity.getTicket().getEvent() != null) {
            qrCodeDto.setEventName(qrCodeEntity.getTicket().getEvent().getVenueName());
        }

        return qrCodeDto;
    }

    @Override
    public QrCodeDto invalidateQrCode(Integer qrCodeId) {
        QrCodeEntity qrCodeEntity = qrCodeRepository.findById(qrCodeId)
                .orElseThrow(() -> new IllegalArgumentException("QR Code not found with ID: " + qrCodeId));

        qrCodeEntity.setIsValid(false);
        qrCodeEntity = qrCodeRepository.save(qrCodeEntity);

        QrCodeDto qrCodeDto = modelMapper.map(qrCodeEntity, QrCodeDto.class);
        qrCodeDto.setTicketId(qrCodeEntity.getTicket().getTicketId());

        if (qrCodeEntity.getTicket().getEvent() != null) {
            qrCodeDto.setEventName(qrCodeEntity.getTicket().getEvent().getName());
        }

        return qrCodeDto;
    }

    @Override
    public QrCodeDto scanQrCode(Integer qrCodeId) {
        QrCodeEntity qrCodeEntity = qrCodeRepository.findById(qrCodeId)
                .orElseThrow(() -> new IllegalArgumentException("QR Code not found with ID: " + qrCodeId));

        if (!qrCodeEntity.getIsValid()) {
            throw new IllegalStateException("QR Code is not valid");
        }

        if (qrCodeEntity.getExpiresAt() != null && qrCodeEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            qrCodeEntity.setIsValid(false);
            qrCodeRepository.save(qrCodeEntity);
            throw new IllegalStateException("QR Code has expired");
        }

        qrCodeEntity.setScannedAt(LocalDateTime.now());
        qrCodeEntity = qrCodeRepository.save(qrCodeEntity);

        QrCodeDto qrCodeDto = modelMapper.map(qrCodeEntity, QrCodeDto.class);
        qrCodeDto.setTicketId(qrCodeEntity.getTicket().getTicketId());

        if (qrCodeEntity.getTicket().getEvent() != null) {
            qrCodeDto.setEventName(qrCodeEntity.getTicket().getEvent().getName());
        }

        return qrCodeDto;
    }

    private String generateQrCodeImage(String content, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}