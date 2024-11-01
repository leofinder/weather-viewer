package com.craftelix.weatherviewer.filter;

import com.craftelix.weatherviewer.entity.Session;
import com.craftelix.weatherviewer.service.SessionService;
import com.craftelix.weatherviewer.service.UserService;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class SessionFilter extends OncePerRequestFilter {

    private static final String SESSION_ID = "sessionId";

    private final SessionService sessionService;

    private final UserService userService;

    @Autowired
    public SessionFilter(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<Cookie> sessionCookie = getSessionCookie(request);

        if (sessionCookie.isEmpty()) {
            response.sendRedirect("/login");
            return;
        }

        UUID sessionId = UUID.fromString(sessionCookie.get().getValue());

        if (isSessionValid(sessionId)) {
            request.setAttribute("user", userService.getUserBySessionId(sessionId));
            filterChain.doFilter(request, response);
        } else {
            sessionService.removeSession(sessionId);
            response.sendRedirect("/login");
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

    private Optional<Cookie> getSessionCookie(HttpServletRequest httpRequest) {
        Optional<Cookie> sessionCookie = Optional.empty();

        Cookie[] cookies = httpRequest.getCookies();

        if (cookies != null) {
            sessionCookie = Arrays.stream(cookies)
                    .filter(cookie -> SESSION_ID.equals(cookie.getName()))
                    .findFirst();
        }

        return sessionCookie;
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

}
