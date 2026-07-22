package com.emailmarketing.controller;

import com.emailmarketing.config.AppConfig;
import com.emailmarketing.config.LoginInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller untuk autentikasi admin (login & logout) berbasis session.
 * Kredensial diambil dari application.properties melalui AppConfig.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AppConfig appConfig;

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null && Boolean.TRUE.equals(session.getAttribute(LoginInterceptor.SESSION_KEY_LOGGED_IN))) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                           @RequestParam String password,
                           HttpServletRequest request,
                           Model model) {

        if (appConfig.getAdminUsername().equals(username) && appConfig.getAdminPassword().equals(password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute(LoginInterceptor.SESSION_KEY_LOGGED_IN, true);
            session.setAttribute("ADMIN_USERNAME", username);
            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Username atau password salah.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout";
    }
}
