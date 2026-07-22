package com.emailmarketing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Model Recipient (penerima email).
 * Class ini merupakan hasil migrasi dari class "Recipient" pada project
 * Java Console Email Marketing lama, dipindahkan ke package model
 * dan digunakan oleh service (CsvReaderService, EmailSenderService).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipient {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private String name;
    private String email;

    /** Status pengiriman untuk baris preview / progress: PENDING, SENT, FAILED */
    private String status = "PENDING";

    /** Pesan error jika pengiriman gagal */
    private String errorMessage;

    /** Apakah format email valid (dicek saat parsing CSV) */
    private boolean validEmail;

    public Recipient(String name, String email) {
        this.name = name;
        this.email = email;
        this.validEmail = isValidEmailFormat(email);
        this.status = "PENDING";
    }

    public static boolean isValidEmailFormat(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
}
