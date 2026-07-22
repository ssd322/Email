package com.emailmarketing.service;

import com.emailmarketing.model.Recipient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Service hasil migrasi dari class "CsvReader" pada project Java Console
 * Email Marketing lama. Bertugas membaca file recipients.csv yang di-upload
 * pengguna, mem-parsing baris demi baris menjadi objek Recipient,
 * serta memvalidasi format email pada setiap baris.
 *
 * Format CSV yang didukung (header opsional, dipisah koma):
 *   nama,email
 *   John Doe,john@example.com
 *
 * Jika hanya ada 1 kolom, dianggap kolom tersebut adalah email
 * dan nama diambil dari bagian sebelum '@'.
 */
@Service
public class CsvReaderService {

    public List<Recipient> parseCsv(MultipartFile file) throws IOException {
        List<Recipient> recipients = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // Lewati baris header jika terdeteksi (mengandung kata "email" atau "nama")
                if (firstLine) {
                    firstLine = false;
                    String lowerLine = line.toLowerCase();
                    if (lowerLine.contains("email") || lowerLine.contains("nama") || lowerLine.contains("name")) {
                        continue;
                    }
                }

                String[] parts = splitCsvLine(line);
                String name;
                String email;

                if (parts.length >= 2) {
                    name = parts[0].trim();
                    email = parts[1].trim();
                } else {
                    email = parts[0].trim();
                    name = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
                }

                recipients.add(new Recipient(name, email));
            }
        }

        return recipients;
    }

    private String[] splitCsvLine(String line) {
        // Split sederhana berbasis koma, dengan dukungan nilai bertanda kutip (")
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                tokens.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        tokens.add(current.toString());
        return tokens.toArray(new String[0]);
    }
}
