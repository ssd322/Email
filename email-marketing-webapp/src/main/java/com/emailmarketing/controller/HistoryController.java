package com.emailmarketing.controller;

import com.emailmarketing.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/riwayat")
    public String riwayatPage(Model model) {
        model.addAttribute("activePage", "riwayat");
        model.addAttribute("historyList", historyService.getAllHistory());
        model.addAttribute("totalSukses", historyService.countSuccess());
        model.addAttribute("totalGagal", historyService.countFailed());
        return "history";
    }

    @PostMapping("/riwayat/hapus")
    public String clearHistory(RedirectAttributes redirectAttributes) {
        historyService.clear();
        redirectAttributes.addFlashAttribute("success", "Riwayat pengiriman berhasil dihapus.");
        return "redirect:/riwayat";
    }
}
