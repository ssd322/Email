package com.emailmarketing.service;

import com.emailmarketing.config.AppConfig;
import com.emailmarketing.model.SmtpConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Menyimpan konfigurasi SMTP yang bisa diubah pengguna melalui halaman SMTP.
 * Nilai awal diambil dari AppConfig (application.properties), lalu dapat
 * di-override saat runtime melalui tombol "Simpan" pada halaman SMTP.
 *
 * Catatan: penyimpanan bersifat in-memory (berlaku selama aplikasi berjalan).
 */
@Service
@RequiredArgsConstructor
public class SmtpConfigService {

    private final AppConfig appConfig;

    private SmtpConfig currentConfig;

    @PostConstruct
    public void init() {
        currentConfig = new SmtpConfig(
                appConfig.getDefaultSmtpHost(),
                appConfig.getDefaultSmtpPort(),
                appConfig.getDefaultSmtpUsername(),
                appConfig.getDefaultSmtpPassword(),
                appConfig.isDefaultSmtpTls()
        );
    }

    public SmtpConfig getCurrentConfig() {
        return currentConfig;
    }

    public void saveConfig(SmtpConfig config) {
        this.currentConfig = config;
    }
}
