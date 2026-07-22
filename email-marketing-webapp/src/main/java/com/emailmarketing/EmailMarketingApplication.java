package com.emailmarketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Entry point aplikasi Email Marketing Web.
 * Migrasi dari aplikasi Java Console Email Marketing (Maven) ke Spring Boot MVC.
 */
@SpringBootApplication
@EnableAsync
public class EmailMarketingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailMarketingApplication.class, args);
    }
}
