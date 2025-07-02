package com.example.labourApp.Controller;

import com.example.labourApp.Models.ResponseDTO;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/aadhaar")
class AadhaarAuthController {

    @PostMapping("/verifyAadhaar")
    public Callable<ResponseEntity<ResponseDTO>> verifyAadhaar(@RequestParam("qrImage") MultipartFile file) {
        return () -> {

            try (InputStream in = file.getInputStream()) {

                BufferedImage bufferedImage = ImageIO.read(in);
                LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                Result result = new MultiFormatReader().decode(bitmap);
                String rawData = result.getText();

                return new ResponseEntity<>(new ResponseDTO(rawData, false, "QR image Decoded Successfully !!"), HttpStatus.OK);
            } catch (NotFoundException e) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "QR code not found in image"), HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to decode Aadhaar QR: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        };

    }
}