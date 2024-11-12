package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.config.TestJdbcConfig;
import com.craftelix.weatherviewer.dto.session.SessionDto;
import com.craftelix.weatherviewer.entity.Session;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.exception.SessionNotFoundException;
import com.craftelix.weatherviewer.mapper.SessionMapperImpl;
import com.craftelix.weatherviewer.repository.SessionRepository;
import com.craftelix.weatherviewer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJdbcConfig.class, SessionService.class, SessionRepository.class, SessionMapperImpl.class,
                UserRepository.class})
@Transactional
@TestPropertySource(properties = "session.expiration.minutes=30")
class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateSession() {
        User user = getUser();
        Long userId = user.getId();
        SessionDto sessionDto = sessionService.createSession(userId);

        assertThat(sessionDto).isNotNull();

        Optional<Session> savedSession = sessionRepository.findById(sessionDto.getId());
        assertThat(savedSession).isPresent();
        assertThat(savedSession.get().getUserId()).isEqualTo(userId);
        assertThat(savedSession.get().getExpiresAt()).isAfter(LocalDateTime.now());
    }

    @Test
    void shouldFindExistingSession() {
        User user = getUser();
        Session session = getSession(user);
        sessionRepository.save(session);

        Session foundSession = sessionService.findSession(session.getId());

        assertThat(foundSession).isNotNull();
        assertThat(foundSession.getId()).isEqualTo(session.getId());
        assertThat(foundSession.getUserId()).isEqualTo(session.getUserId());
    }

    @Test
    void shouldRemoveSessionIfExists() {
        User user = getUser();
        Session session = getSession(user);
        sessionRepository.save(session);

        UUID sessionId = session.getId();
        sessionService.removeSession(sessionId);

        assertThat(sessionId).isNotNull();

        Optional<Session> deletedSession = sessionRepository.findById(sessionId);
        assertThat(deletedSession).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenSessionNotFound() {
        UUID nonexistentSessionId = UUID.randomUUID();

        assertThatThrownBy(() -> sessionService.findSession(nonexistentSessionId))
                .isInstanceOf(SessionNotFoundException.class);
    }

    private User getUser() {
        User user = User.builder()
                .username("testuser")
                .password("password")
                .build();
        return userRepository.save(user);
    }

    private Session getSession(User user) {
        return Session.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
    }
}