package com.example.Transfero.service;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
public class GoogleSheetsService {
    private static final String APPLICATION_NAME = "Transfero";
    private static final String SPREADSHEET_ID = "1RhzvW6OPjIdxMZn9SbvNgqCOUJnRMb6SsdNYjxn6QpA"; // ← Replace this
    private static final String RANGE = "Sheet1!A:E";

    public void saveContact(String name, String email,String message) throws Exception {
        InputStream in;
        String base64 = System.getenv("GOOGLE_CREDENTIALS");

        if (base64 != null && !base64.isEmpty()) {
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            in = new ByteArrayInputStream(decodedBytes);
        } else {
            in = getClass().getResourceAsStream("/credentials.json"); // Local fallback
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));

        Sheets sheetsService = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();

        ValueRange appendBody = new ValueRange()
                .setValues(List.of(List.of(name, email, message, LocalDateTime.now().toString())));

        sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, RANGE, appendBody)
                .setValueInputOption("RAW")
                .execute();
    }
}
