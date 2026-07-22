package com.emailmarketing.service;

import com.emailmarketing.model.EmailTemplate;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * Menyimpan template email (subject + isi HTML) yang digunakan saat
 * proses pengiriman massal. Bersifat in-memory selama aplikasi berjalan.
 */
@Service
public class TemplateService {

    private EmailTemplate currentTemplate;

    @PostConstruct
    public void init() {
        currentTemplate = new EmailTemplate(
                "Promo Spesial Untuk Anda!",
                "<h2>Halo Sahabat,</h2>" +
                        "<p>Terima kasih telah menjadi bagian dari kami. " +
                        "Nikmati penawaran spesial minggu ini.</p>" +
                        "<p>Salam hangat,<br/>Tim Marketing</p>"
        );
    }

    public EmailTemplate getCurrentTemplate() {
        return currentTemplate;
    }

    public void saveTemplate(EmailTemplate template) {
        this.currentTemplate = template;
    }
}
