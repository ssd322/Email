package com.emailmarketing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model riwayat pengiriman email (satu baris per penerima).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendHistory {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private LocalDateTime tanggal;
    private String emailTujuan;
    private String status; // SUKSES / GAGAL
    private String pesanError;

    public String getTanggalFormatted() {
        return tanggal != null ? tanggal.format(FORMATTER) : "-";
    }
}
