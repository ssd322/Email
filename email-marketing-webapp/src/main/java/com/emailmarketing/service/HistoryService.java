package com.emailmarketing.service;

import com.emailmarketing.model.SendHistory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Menyimpan riwayat pengiriman email (tanggal, email tujuan, status, pesan error).
 * Data bersifat in-memory, terus terkumpul sepanjang aplikasi berjalan.
 */
@Service
public class HistoryService {

    private final List<SendHistory> historyList = new CopyOnWriteArrayList<>();

    public void addSuccess(String email) {
        historyList.add(0, new SendHistory(LocalDateTime.now(), email, "SUKSES", null));
    }

    public void addFailure(String email, String errorMessage) {
        historyList.add(0, new SendHistory(LocalDateTime.now(), email, "GAGAL", errorMessage));
    }

    public List<SendHistory> getAllHistory() {
        return Collections.unmodifiableList(new ArrayList<>(historyList));
    }

    public long countSuccess() {
        return historyList.stream().filter(h -> "SUKSES".equals(h.getStatus())).count();
    }

    public long countFailed() {
        return historyList.stream().filter(h -> "GAGAL".equals(h.getStatus())).count();
    }

    public void clear() {
        historyList.clear();
    }
}
