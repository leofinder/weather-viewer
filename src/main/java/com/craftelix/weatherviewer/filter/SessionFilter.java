package com.craftelix.weatherviewer.filter;

import com.craftelix.weatherviewer.entity.Session;
import com.craftelix.weatherviewer.service.SessionService;
import com.craftelix.weatherviewer.service.UserService;
import com.craftelix.weatherviewer.util.CookiesUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class SessionFilter extends OncePerRequestFilter {

    private final SessionService sessionService;

    private final UserService userService;

    @Autowired
    public SessionFilter(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isApiRequest = isApiRequest(request);

        Optional<Cookie> sessionCookie = CookiesUtil.findCookie(request.getCookies(), CookiesUtil.SESSION_ID);
        if (sessionCookie.isEmpty()) {
            handleUnauthorized(request, response, isApiRequest);
            return;
        }

        UUID sessionId = UUID.fromString(sessionCookie.get().getValue());

        if (isSessionValid(sessionId)) {
            request.setAttribute("user", userService.getUserBySessionId(sessionId));
            filterChain.doFilter(request, response);
        } else {
            sessionService.removeSession(sessionId);
            Cookie cookie = CookiesUtil.createCookieToDelete(CookiesUtil.SESSION_ID);
            response.addCookie(cookie);
            handleUnauthorized(request, response, isApiRequest);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        boolean shouldFilter = true;
        String path = request.getRequestURI();
        if ("/login".equals(path) || "/logout".equals(path) || "/signup".equals(path) || path.contains("/resources/")) {
            shouldFilter = false;
        }
        return !shouldFilter;
    }

    private boolean isSessionValid(UUID sessionId) {
        boolean isValid = false;

        Session session = sessionService.findSession(sessionId);
        LocalDateTime expiresAt = session.getExpiresAt();
        if (expiresAt.isAfter(LocalDateTime.now())) {
            isValid = true;
        }

        return isValid;
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
