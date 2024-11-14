package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.config.TestJdbcConfig;
import com.craftelix.weatherviewer.dto.user.UserDto;
import com.craftelix.weatherviewer.dto.user.UserLoginDto;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.exception.InvalidPasswordException;
import com.craftelix.weatherviewer.exception.UserNotFoundException;
import com.craftelix.weatherviewer.mapper.UserMapperImpl;
import com.craftelix.weatherviewer.repository.UserRepository;
import com.craftelix.weatherviewer.util.PasswordHashing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestJdbcConfig.class, AuthService.class, UserRepository.class, UserMapperImpl.class})
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        createAndSaveUser();
    }

    @Test
    void shouldAuthenticateValidUser() {
        UserLoginDto userLoginDto = new UserLoginDto("testuser", "password");

        UserDto result = authService.authenticate(userLoginDto);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserLoginDto loginDto = new UserLoginDto("nonexistent", "password");

        assertThatThrownBy(() -> authService.authenticate(loginDto))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenPasswordInvalid() {
        UserLoginDto loginDto = new UserLoginDto("testuser", "wrongpassword");

        assertThatThrownBy(() -> authService.authenticate(loginDto))
                .isInstanceOf(InvalidPasswordException.class);
    }

    private void createAndSaveUser() {
        User user = User.builder()
                .username("testuser")
                .password(PasswordHashing.hashPassword("password"))
                .build();
        userRepository.save(user);
    }
}
