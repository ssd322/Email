package com.emailmarketing.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor yang memastikan seluruh halaman (kecuali /login dan resource statis)
 * hanya bisa diakses oleh admin yang sudah login (session-based authentication).
 */
public class LoginInterceptor implements HandlerInterceptor {

    public static final String SESSION_KEY_LOGGED_IN = "LOGGED_IN_ADMIN";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession(false);
        boolean loggedIn = session != null && Boolean.TRUE.equals(session.getAttribute(SESSION_KEY_LOGGED_IN));

        if (!loggedIn) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }
}
