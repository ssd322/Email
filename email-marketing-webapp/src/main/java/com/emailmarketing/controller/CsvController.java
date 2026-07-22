package com.emailmarketing.controller;

import com.emailmarketing.model.Recipient;
import com.emailmarketing.service.CsvReaderService;
import com.emailmarketing.service.RecipientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CsvController {

    private final CsvReaderService csvReaderService;
    private final RecipientService recipientService;

    @GetMapping("/upload-csv")
    public String uploadPage(Model model) {
        model.addAttribute("activePage", "upload-csv");
        model.addAttribute("recipients", recipientService.getAllRecipients());
        model.addAttribute("totalValid", recipientService.countValid());
        model.addAttribute("totalInvalid", recipientService.countInvalid());
        return "upload-csv";
    }

    @PostMapping("/upload-csv")
    public String handleUpload(@RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Silakan pilih file CSV terlebih dahulu.");
            return "redirect:/upload-csv";
        }

        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            redirectAttributes.addFlashAttribute("error", "Format file harus .csv");
            return "redirect:/upload-csv";
        }

        try {
            List<Recipient> recipients = csvReaderService.parseCsv(file);
            recipientService.setRecipients(recipients);

            long invalid = recipients.stream().filter(r -> !r.isValidEmail()).count();
            if (invalid > 0) {
                redirectAttributes.addFlashAttribute("warning",
                        "File berhasil diupload. " + invalid + " email dengan format tidak valid ditandai merah.");
            } else {
                redirectAttributes.addFlashAttribute("success",
                        "File berhasil diupload. " + recipients.size() + " penerima ditemukan.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Gagal membaca file CSV: " + e.getMessage());
        }

        return "redirect:/upload-csv";
    }

    @PostMapping("/upload-csv/clear")
    public String clearRecipients(RedirectAttributes redirectAttributes) {
        recipientService.clear();
        redirectAttributes.addFlashAttribute("success", "Daftar penerima berhasil dikosongkan.");
        return "redirect:/upload-csv";
    }
}
