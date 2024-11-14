package com.craftelix.weatherviewer.interceptor;

import com.craftelix.weatherviewer.entity.Session;
import com.craftelix.weatherviewer.service.SessionService;
import com.craftelix.weatherviewer.service.UserService;
import com.craftelix.weatherviewer.util.CookiesUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isApiRequest = isApiRequest(request);

        Optional<Cookie> sessionCookie = CookiesUtil.findCookie(request.getCookies(), CookiesUtil.SESSION_ID);
        if (sessionCookie.isEmpty()) {
            handleUnauthorized(request, response, isApiRequest);
            return false;
        }

        UUID sessionId = UUID.fromString(sessionCookie.get().getValue());

        if (isSessionValid(sessionId)) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("user", userService.getUserBySessionId(sessionId));
            return true;
        } else {
            sessionService.removeSession(sessionId);
            Cookie cookie = CookiesUtil.createCookieToDelete(CookiesUtil.SESSION_ID);
            response.addCookie(cookie);
            handleUnauthorized(request, response, isApiRequest);
            return false;
        }
    }

    private boolean isSessionValid(UUID sessionId) {
        Session session = sessionService.findSession(sessionId);
        return session != null && session.getExpiresAt().isAfter(LocalDateTime.now());
    }

    private boolean isApiRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        String acceptHeader = request.getHeader("Accept");
        String contentType = request.getHeader("Content-Type");

        return "XMLHttpRequest".equals(requestedWith)
                || (acceptHeader != null && acceptHeader.contains("application/json"))
                || (contentType != null && contentType.contains("application/json"))
                || request.getRequestURI().startsWith("/api/");
    }

    private void handleUnauthorized(HttpServletRequest request, HttpServletResponse response, boolean isApiRequest) throws IOException {
        if (isApiRequest) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Please login\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.sendRedirect("/login");
        }
    }
}
