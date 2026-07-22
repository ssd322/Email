package com.emailmarketing.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model konfigurasi SMTP.
 * Hasil migrasi dari class "Config" pada project Java Console lama
 * (bagian yang menyimpan pengaturan SMTP), sekarang dipakai sebagai
 * form-backing object pada halaman SMTP.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmtpConfig {

    @NotBlank(message = "SMTP Host wajib diisi")
    private String host;

    @NotNull(message = "SMTP Port wajib diisi")
    @Min(value = 1, message = "Port tidak valid")
    @Max(value = 65535, message = "Port tidak valid")
    private Integer port;

    @NotBlank(message = "Username wajib diisi")
    private String username;

    @NotBlank(message = "Password wajib diisi")
    private String password;

    private boolean tls;
}
