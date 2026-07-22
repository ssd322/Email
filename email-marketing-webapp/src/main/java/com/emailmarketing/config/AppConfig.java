package com.emailmarketing.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Konfigurasi aplikasi (kredensial admin, default SMTP, delay pengiriman).
 * Merupakan hasil migrasi dari class "Config" pada project Java Console
 * Email Marketing lama. Nilai awal diambil dari application.properties,
 * kemudian beberapa nilai (SMTP) bisa di-override saat runtime lewat
 * halaman SMTP menggunakan SmtpConfigService.
 */
@Component
@Getter
@Setter
public class AppConfig {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${smtp.host}")
    private String defaultSmtpHost;

    @Value("${smtp.port}")
    private int defaultSmtpPort;

    @Value("${smtp.username}")
    private String defaultSmtpUsername;

    @Value("${smtp.password}")
    private String defaultSmtpPassword;

    @Value("${smtp.tls}")
    private boolean defaultSmtpTls;

    @Value("${mail.send.delay-ms:1000}")
    private long defaultSendDelayMs;
}
