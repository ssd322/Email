package com.emailmarketing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DTO untuk response JSON progress pengiriman email,
 * dikonsumsi oleh JavaScript (polling AJAX) di halaman Kirim Email.
 */
@Data
@AllArgsConstructor
public class ProgressResponse {
    private boolean running;
    private boolean finished;
    private int total;
    private int sent;
    private int failed;
    private int processed;
    private int percentage;
    private List<String> logs;
}
