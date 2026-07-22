package com.emailmarketing.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Menyimpan status progress pengiriman email secara real-time.
 * Di-poll oleh halaman "Kirim Email" via AJAX untuk menampilkan progress bar
 * dan status berhasil/gagal setiap penerima.
 *
 * Catatan: class ini TIDAK menggunakan Lombok @Data karena field-nya berupa
 * tipe atomik (AtomicInteger/AtomicBoolean) yang butuh accessor kustom
 * (mengembalikan nilai primitif, bukan objek atomiknya).
 */
public class SendProgress {

    private final AtomicInteger total = new AtomicInteger(0);
    private final AtomicInteger sent = new AtomicInteger(0);
    private final AtomicInteger failed = new AtomicInteger(0);
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean finished = new AtomicBoolean(true);

    // list log realtime, format: "OK|nama|email|pesan" atau "FAIL|nama|email|pesan"
    private final List<String> logs = new ArrayList<>();

    public synchronized void reset(int totalRecipient) {
        total.set(totalRecipient);
        sent.set(0);
        failed.set(0);
        running.set(true);
        finished.set(false);
        logs.clear();
    }

    public synchronized void addLog(String message) {
        logs.add(message);
    }

    public synchronized void finish() {
        running.set(false);
        finished.set(true);
    }

    public void incrementSent() {
        sent.incrementAndGet();
    }

    public void incrementFailed() {
        failed.incrementAndGet();
    }

    public int getTotal() {
        return total.get();
    }

    public int getSent() {
        return sent.get();
    }

    public int getFailed() {
        return failed.get();
    }

    public boolean isRunning() {
        return running.get();
    }

    public boolean isFinished() {
        return finished.get();
    }

    public int getProcessed() {
        return sent.get() + failed.get();
    }

    public int getPercentage() {
        int t = total.get();
        if (t == 0) return 0;
        return (int) Math.round((getProcessed() * 100.0) / t);
    }

    public synchronized List<String> getLogs() {
        return Collections.unmodifiableList(new ArrayList<>(logs));
    }
}
