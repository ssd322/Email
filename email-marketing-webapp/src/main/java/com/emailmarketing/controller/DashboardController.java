package com.emailmarketing.controller;

import com.emailmarketing.service.HistoryService;
import com.emailmarketing.service.RecipientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final HistoryService historyService;
    private final RecipientService recipientService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("totalTerkirim", historyService.countSuccess());
        model.addAttribute("totalGagal", historyService.countFailed());
        model.addAttribute("totalPenerima", recipientService.countTotal());
        model.addAttribute("recentHistory", historyService.getAllHistory().stream().limit(5).toList());
        return "dashboard";
    }
}
