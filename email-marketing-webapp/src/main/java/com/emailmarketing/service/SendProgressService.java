package com.emailmarketing.service;

import com.emailmarketing.model.SendProgress;
import org.springframework.stereotype.Service;

/**
 * Menyediakan objek SendProgress singleton yang digunakan untuk melacak
 * progress pengiriman email secara realtime (dipoll oleh frontend via AJAX).
 */
@Service
public class SendProgressService {

    private final SendProgress progress = new SendProgress();

    public SendProgress getProgress() {
        return progress;
    }
}
