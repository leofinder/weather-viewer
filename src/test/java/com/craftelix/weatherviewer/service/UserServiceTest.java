package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.config.TestJdbcConfig;
import com.craftelix.weatherviewer.dto.user.UserDto;
import com.craftelix.weatherviewer.dto.user.UserSignupDto;
import com.craftelix.weatherviewer.entity.Session;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.exception.SessionNotFoundException;
import com.craftelix.weatherviewer.exception.UserAlreadyExistException;
import com.craftelix.weatherviewer.mapper.UserMapperImpl;
import com.craftelix.weatherviewer.repository.SessionRepository;
import com.craftelix.weatherviewer.repository.UserRepository;
import com.craftelix.weatherviewer.util.PasswordHashing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJdbcConfig.class, UserService.class, UserRepository.class, UserMapperImpl.class,
        SessionRepository.class})
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        sessionRepository.deleteAll();
    }

    @Test
    void shouldSaveNewUser() {
        UserSignupDto userSignupDto = new UserSignupDto("newuser", "password", "password");
        userService.save(userSignupDto);

        Optional<User> savedUser = userRepository.findByUsername("newuser");

        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getUsername()).isEqualTo("newuser");
        assertThat(PasswordHashing.checkPassword("password", savedUser.get().getPassword())).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        User existingUser = User.builder()
                .username("existinguser")
                .password(PasswordHashing.hashPassword("password"))
                .build();
        userRepository.save(existingUser);

        UserSignupDto userSignupDto = new UserSignupDto("existinguser", "password", "password");

        assertThatThrownBy(() -> userService.save(userSignupDto))
                .isInstanceOf(UserAlreadyExistException.class);
    }

    @Test
    void shouldReturnUserBySessionId() {
        User user = User.builder()
                .username("testuser")
                .password(PasswordHashing.hashPassword("password"))
                .build();
        userRepository.save(user);

        Session session = Session.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();
        sessionRepository.save(session);

        UserDto userDto = userService.getUserBySessionId(session.getId());

        assertThat(userDto).isNotNull();
        assertThat(userDto.getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldThrowExceptionWhenSessionIdNotFound() {
        UUID nonExistentSessionId = UUID.randomUUID();

        assertThatThrownBy(() -> userService.getUserBySessionId(nonExistentSessionId))
                .isInstanceOf(SessionNotFoundException.class);
    }
}
