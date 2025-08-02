package com.example.Transfero.controller;

import com.example.Transfero.model.FileMeta;
import com.example.Transfero.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.example.Transfero.util.ShortCodeGenerator.generateCode;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<List<FileMeta>> uploadMultipleFiles(@RequestParam("file") MultipartFile[] files) {
        String groupCode = generateCode();
        List<FileMeta> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                FileMeta meta = fileService.storeFile(file,groupCode);
                uploadedFiles.add(meta);
            } catch (Exception e) {
                // Optionally log or skip this file
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok(uploadedFiles);
    }


    @GetMapping("/download/{code}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String code) {
        Optional<FileMeta> optional = fileService.getFileByCode(code);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        FileMeta meta = optional.get();
        try {
            byte[] data = fileService.downloadFile(meta);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(meta.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + meta.getOriginalFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download/group/{groupCode}")
    public void downloadGroupZip(@PathVariable String groupCode, HttpServletResponse response) {
        List<FileMeta> files = fileService.getFilesByGroupCode(groupCode);

        if (files.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=Transfero-" + groupCode + ".zip");

        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            for (FileMeta file : files) {
                ZipEntry zipEntry = new ZipEntry(file.getOriginalFileName());
                zipOut.putNextEntry(zipEntry);
                zipOut.write(file.getFileData());
                zipOut.closeEntry();
            }
            zipOut.finish();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

}
