package com.emailmarketing.controller;

import com.emailmarketing.model.ProgressResponse;
import com.emailmarketing.model.Recipient;
import com.emailmarketing.model.SendProgress;
import com.emailmarketing.service.EmailSenderService;
import com.emailmarketing.service.RecipientService;
import com.emailmarketing.service.SendProgressService;
import com.emailmarketing.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SendEmailController {

    private final RecipientService recipientService;
    private final TemplateService templateService;
    private final EmailSenderService emailSenderService;
    private final SendProgressService sendProgressService;

    @GetMapping("/kirim-email")
    public String kirimEmailPage(Model model) {
        model.addAttribute("activePage", "kirim-email");
        model.addAttribute("totalPenerimaValid", recipientService.countValid());
        model.addAttribute("totalPenerima", recipientService.countTotal());
        model.addAttribute("templateSubject", templateService.getCurrentTemplate().getSubject());
        model.addAttribute("defaultDelay", emailSenderService.getDefaultDelayMs());

        SendProgress progress = sendProgressService.getProgress();
        model.addAttribute("isRunning", progress.isRunning());
        return "send-email";
    }

    @PostMapping("/kirim-email/mulai")
    @ResponseBody
    public ProgressResponse mulaiKirim(@RequestParam(defaultValue = "1000") long delayMs) {
        SendProgress progress = sendProgressService.getProgress();

        if (progress.isRunning()) {
            return toResponse(progress);
        }

        List<Recipient> validRecipients = recipientService.getValidRecipients();
        emailSenderService.sendBulkEmailAsync(validRecipients, delayMs);

        return toResponse(progress);
    }

    @GetMapping("/kirim-email/progress")
    @ResponseBody
    public ProgressResponse progress() {
        return toResponse(sendProgressService.getProgress());
    }

    private ProgressResponse toResponse(SendProgress progress) {
        return new ProgressResponse(
                progress.isRunning(),
                progress.isFinished(),
                progress.getTotal(),
                progress.getSent(),
                progress.getFailed(),
                progress.getProcessed(),
                progress.getPercentage(),
                progress.getLogs()
        );
    }
}
