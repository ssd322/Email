package com.emailmarketing.controller;

import com.emailmarketing.model.EmailTemplate;
import com.emailmarketing.service.TemplateService;
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
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping("/template")
    public String templatePage(Model model) {
        model.addAttribute("activePage", "template");
        if (!model.containsAttribute("emailTemplate")) {
            model.addAttribute("emailTemplate", templateService.getCurrentTemplate());
        }
        return "template";
    }

    @PostMapping("/template/save")
    public String saveTemplate(@Valid @ModelAttribute("emailTemplate") EmailTemplate emailTemplate,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "template");
            model.addAttribute("error", "Subject dan isi email wajib diisi.");
            return "template";
        }

        templateService.saveTemplate(emailTemplate);
        redirectAttributes.addFlashAttribute("success", "Template email berhasil disimpan.");
        return "redirect:/template";
    }
}
