package com.example.Transfero.controller;

import com.example.Transfero.dto.ContactRequest;
import com.example.Transfero.service.GoogleSheetsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private GoogleSheetsService googleSheetsService;

    @PostMapping
    public ResponseEntity<String> submitContact(@Valid @RequestBody ContactRequest request) {
        try {
            googleSheetsService.saveContact(
                    request.getName(),
                    request.getEmail(),
                    request.getMessage()
            );
            return ResponseEntity.ok("Message saved to Google Sheet!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to save message.");
        }
    }
}
