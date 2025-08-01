package com.example.Transfero.service;

import com.example.Transfero.model.FileMeta;
import com.example.Transfero.repository.FileMetaRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;

import static com.example.Transfero.util.ShortCodeGenerator.generateCode;

@Service
public class FileService {

    @Autowired
    private FileMetaRepository fileRepo;

    @Value("${app.base-url}")
    private String baseUrl;



    public FileMeta storeFile(MultipartFile file) throws Exception {
        // Create short code and download URL
        String shortCode = generateCode();
        String downloadUrl = baseUrl + "/api/download/" + shortCode;

        // QR code generation
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        var bitMatrix = qrCodeWriter.encode(downloadUrl, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream qrOut = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", qrOut);
        String qrBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(qrOut.toByteArray());

        // Read file content as byte[]
        byte[] fileBytes = file.getBytes();

        // Store file meta
        FileMeta meta = new FileMeta();
        meta.setOriginalFileName(file.getOriginalFilename());
        meta.setContentType(file.getContentType());
        meta.setSize(file.getSize());
        meta.setShortCode(shortCode);
        meta.setDownloadUrl(downloadUrl);
        meta.setQrCodeBase64(qrBase64);
        meta.setCreatedAt(new Date());
        meta.setFileData(fileBytes); // set file bytes into MongoDB

        return fileRepo.save(meta);
    }

    public Optional<FileMeta> getFileByCode(String code) {
        return fileRepo.findByShortCode(code);
    }

    public byte[] downloadFile(FileMeta meta) throws IOException {
        return meta.getFileData();
    }

}
