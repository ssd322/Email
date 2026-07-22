package com.emailmarketing.service;

import com.emailmarketing.model.Recipient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Menyimpan daftar penerima (recipients) hasil parsing CSV secara in-memory.
 * Dipakai bersama oleh CsvController (upload) dan SendEmailController (kirim).
 */
@Service
public class RecipientService {

    private final List<Recipient> recipients = new CopyOnWriteArrayList<>();

    public void setRecipients(List<Recipient> newRecipients) {
        recipients.clear();
        recipients.addAll(newRecipients);
    }

    public List<Recipient> getAllRecipients() {
        return Collections.unmodifiableList(new ArrayList<>(recipients));
    }

    public List<Recipient> getValidRecipients() {
        List<Recipient> result = new ArrayList<>();
        for (Recipient r : recipients) {
            if (r.isValidEmail()) {
                result.add(r);
            }
        }
        return result;
    }

    public int countValid() {
        return getValidRecipients().size();
    }

    public int countInvalid() {
        return recipients.size() - countValid();
    }

    public int countTotal() {
        return recipients.size();
    }

    public void clear() {
        recipients.clear();
    }
}
