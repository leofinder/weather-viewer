package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.dto.session.SessionDto;
import com.craftelix.weatherviewer.entity.Session;
import com.craftelix.weatherviewer.exception.SessionNotFoundException;
import com.craftelix.weatherviewer.mapper.SessionMapper;
import com.craftelix.weatherviewer.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    private final SessionMapper sessionMapper;

    @Value("${session.expiration.minutes}")
    private int expirationMinutes;

    public SessionDto createSession(Long userId) {
        Session session = Session.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .expiresAt(LocalDateTime.now().plusMinutes(expirationMinutes))
                .build();
        session = sessionRepository.save(session);

        return sessionMapper.toDto(session);
    }

    public void removeSession(UUID id) {
        if (sessionRepository.findById(id).isPresent()) {
            sessionRepository.deleteById(id);
        }
    }

    public Session findSession(UUID id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Session with id '{}' not found", id);
                    return new SessionNotFoundException(String.format("Session with id %s not found", id));
                });
    }
}
