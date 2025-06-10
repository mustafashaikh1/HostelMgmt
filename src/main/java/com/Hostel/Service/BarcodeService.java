package com.Hostel.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class BarcodeService {

    // Static URL to be used in the barcode
    private static final String BASE_URL = "https://pjsofttech.in/juii_girls_hostel_enquiryform";

    // Static URL to be used in the barcode


    // Method to generate barcode without requiring institute code
    public byte[] generateBarcode() {
        try {
            // Static URL is now used directly without any instituteCode
            String data = BASE_URL;

            QRCodeWriter barcodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = barcodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating barcode", e);
        }
    }
}