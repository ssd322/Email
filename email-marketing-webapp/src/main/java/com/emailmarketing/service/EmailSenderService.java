package com.emailmarketing.service;

import com.emailmarketing.config.AppConfig;
import com.emailmarketing.model.EmailTemplate;
import com.emailmarketing.model.Recipient;
import com.emailmarketing.model.SendProgress;
import com.emailmarketing.model.SmtpConfig;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

/**
 * Service hasil migrasi dari class "EmailSender" pada project Java Console
 * Email Marketing lama. Bertanggung jawab membangun koneksi SMTP (Jakarta Mail)
 * dan mengirimkan email ke seluruh penerima yang valid, satu per satu,
 * dengan delay yang dapat diatur, sambil memperbarui SendProgress secara
 * realtime dan mencatat riwayat pengiriman ke HistoryService.
 */
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private static final Logger log = LoggerFactory.getLogger(EmailSenderService.class);

    private final SmtpConfigService smtpConfigService;
    private final TemplateService templateService;
    private final HistoryService historyService;
    private final SendProgressService sendProgressService;
    private final AppConfig appConfig;

    /**
     * Membangun jakarta.mail.Session berdasarkan konfigurasi SMTP saat ini.
     */
    private Session buildMailSession(SmtpConfig cfg) {
        Properties props = new Properties();
        props.put("mail.smtp.host", cfg.getHost());
        props.put("mail.smtp.port", String.valueOf(cfg.getPort()));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", String.valueOf(cfg.isTls()));
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        return Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(cfg.getUsername(), cfg.getPassword());
            }
        });
    }

    /**
     * Mengirim satu email ke penerima tertentu. Melempar MessagingException jika gagal.
     */
    public void sendSingleEmail(Session session, SmtpConfig cfg, Recipient recipient, EmailTemplate template)
            throws MessagingException {

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(cfg.getUsername()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient.getEmail()));
        message.setSubject(template.getSubject());

        String personalizedContent = template.getHtmlContent()
                .replace("{{nama}}", recipient.getName() == null ? "" : recipient.getName())
                .replace("{{email}}", recipient.getEmail());

        message.setContent(personalizedContent, "text/html; charset=UTF-8");

        Transport.send(message);
    }

    /**
     * Mengirim email ke seluruh penerima valid secara asynchronous (background thread),
     * memperbarui progress secara realtime, dan menyimpan hasilnya ke riwayat.
     *
     * @param recipients daftar penerima yang valid
     * @param delayMs    delay antar pengiriman email (milidetik)
     */
    @Async("emailTaskExecutor")
    public void sendBulkEmailAsync(List<Recipient> recipients, long delayMs) {
        SendProgress progress = sendProgressService.getProgress();
        progress.reset(recipients.size());

        SmtpConfig cfg = smtpConfigService.getCurrentConfig();
        EmailTemplate template = templateService.getCurrentTemplate();
        Session session = buildMailSession(cfg);

        for (Recipient recipient : recipients) {
            try {
                sendSingleEmail(session, cfg, recipient, template);

                recipient.setStatus("SENT");
                progress.incrementSent();
                historyService.addSuccess(recipient.getEmail());
                progress.addLog("OK|" + recipient.getName() + "|" + recipient.getEmail() + "|Terkirim");

                log.info("Email berhasil dikirim ke {}", recipient.getEmail());
            } catch (Exception e) {
                recipient.setStatus("FAILED");
                recipient.setErrorMessage(e.getMessage());
                progress.incrementFailed();
                historyService.addFailure(recipient.getEmail(), e.getMessage());
                progress.addLog("FAIL|" + recipient.getName() + "|" + recipient.getEmail() + "|" + e.getMessage());

                log.warn("Gagal mengirim email ke {}: {}", recipient.getEmail(), e.getMessage());
            }

            try {
                if (delayMs > 0) {
                    Thread.sleep(delayMs);
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        progress.finish();
    }

    public long getDefaultDelayMs() {
        return appConfig.getDefaultSendDelayMs();
    }
}
