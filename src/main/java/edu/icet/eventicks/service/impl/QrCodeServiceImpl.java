package edu.icet.eventicks.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import edu.icet.eventicks.dto.QrCodeDto;
import edu.icet.eventicks.dto.TicketDto;
import edu.icet.eventicks.entity.QrCodeEntity;
import edu.icet.eventicks.repository.QrCodeRepository;
import edu.icet.eventicks.service.QrCodeService;
import edu.icet.eventicks.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

    private final TicketService ticketService;
    private final QrCodeRepository qrCodeRepository;
    private final ModelMapper modelMapper;

    @Value("${qr.code.image.directory}")
    private String qrCodeImageDirectory;

    @Override
    public QrCodeDto generateQrCode(Integer ticketId) {
        TicketDto ticketById = ticketService.getTicketById(ticketId);

        String qrValue = "Ticket_" + ticketId +
                "\nEvent: " + ticketById.getEventName() +
                "\nPrice: " + ticketById.getPrice();

        try {
            BufferedImage qrImage = generateQRCodeImage(qrValue);
            String imageUrl = saveQrCodeImage(qrImage, ticketId);

            QrCodeEntity qrCodeEntity = new QrCodeEntity();
            qrCodeEntity.setTicketId(ticketId);
            qrCodeEntity.setQrValue(qrValue);
            qrCodeEntity.setEventName(ticketById.getEventName());
            qrCodeEntity.setImageUrl(imageUrl);
            qrCodeEntity = qrCodeRepository.save(qrCodeEntity);

            return modelMapper.map(qrCodeEntity, QrCodeDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR Code", e);
        }
    }


    private BufferedImage generateQRCodeImage(String qrValue) throws Exception {
        Map<EncodeHintType, Object> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                qrValue,
                BarcodeFormat.QR_CODE,
                200, 200, hintMap
        );

        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 200, 200);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                if (bitMatrix.get(i, j)) {
                    image.setRGB(i, j, 0x000000);
                } else {
                    image.setRGB(i, j, 0xFFFFFF);
                }
            }
        }

        return image;
    }

    private String saveQrCodeImage(BufferedImage qrImage, Integer ticketId) throws Exception {
        Path path = Paths.get(qrCodeImageDirectory);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String imagePath = path + File.separator + "qr_code_" + ticketId + ".png";
        File qrFile = new File(imagePath);

        ImageIO.write(qrImage, "PNG", qrFile);

        return imagePath;
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
