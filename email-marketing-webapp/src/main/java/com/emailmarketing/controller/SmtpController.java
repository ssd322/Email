package com.emailmarketing.controller;

import com.emailmarketing.model.SmtpConfig;
import com.emailmarketing.service.SmtpConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class SmtpController {

    private final SmtpConfigService smtpConfigService;

    @GetMapping("/smtp")
    public String smtpPage(Model model) {
        model.addAttribute("activePage", "smtp");
        if (!model.containsAttribute("smtpConfig")) {
            model.addAttribute("smtpConfig", smtpConfigService.getCurrentConfig());
        }
        return "smtp";
    }

    @PostMapping("/smtp/save")
    public String saveSmtp(@Valid @ModelAttribute("smtpConfig") SmtpConfig smtpConfig,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "smtp");
            model.addAttribute("error", "Periksa kembali form SMTP, ada data yang belum valid.");
            return "smtp";
        }

        smtpConfigService.saveConfig(smtpConfig);
        redirectAttributes.addFlashAttribute("success", "Konfigurasi SMTP berhasil disimpan.");
        return "redirect:/smtp";
    }
}
