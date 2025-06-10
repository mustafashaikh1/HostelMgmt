package com.Hostel.Controller;


import com.Hostel.Service.BarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/public")
//@CrossOrigin("http://localhost:3000")
@CrossOrigin(origins = {
        "https://pjsofttech.in",
        "https://live.ooacademy.co.in",
        "https://course.yashodapublication.com",
        "https://lokrajyaacademy.com",
        "http://localhost:3000"
})
public class BarcodeController {

    @Autowired
    private BarcodeService barcodeService;

    @GetMapping("/generate-qrcode")
    public ResponseEntity<byte[]> generateBarcode() {
        byte[] barcodeImage = barcodeService.generateBarcode(); // No parameters needed

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(barcodeImage.length);

        return ResponseEntity.ok().headers(headers).body(barcodeImage);
    }

}