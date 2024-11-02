package com.craftelix.weatherviewer.util;

import jakarta.servlet.http.Cookie;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@UtilityClass
public class CookiesUtil {

    public static final String SESSION_ID = "sessionId";

    public static Cookie createCookie(String key, String value, LocalDateTime expiresAt) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        int maxAge = expiresAt == null ? 0 : (int) Duration.between(LocalDateTime.now(), expiresAt).getSeconds();
        cookie.setMaxAge(maxAge);

        return cookie;
    }

    public static Cookie createCookieToDelete(String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public static Optional<Cookie> findCookie(Cookie[] cookies, String key) {
        Optional<Cookie> cookie = Optional.empty();

        if (cookies != null && key != null) {
            cookie = Arrays.stream(cookies)
                    .filter(c -> key.equals(c.getName()))
                    .findFirst();
        }

        return cookie;
    }
}
