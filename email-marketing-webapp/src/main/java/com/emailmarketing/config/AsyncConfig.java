package com.emailmarketing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Konfigurasi thread pool khusus untuk proses pengiriman email di background,
 * sehingga halaman "Kirim Email" tidak blocking dan bisa menampilkan progress
 * secara realtime melalui polling AJAX.
 */
@Configuration
public class AsyncConfig {

    @Bean(name = "emailTaskExecutor")
    public Executor emailTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("EmailSender-");
        executor.initialize();
        return executor;
    }
}
