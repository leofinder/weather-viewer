package com.craftelix.weatherviewer.util;

import com.craftelix.weatherviewer.entity.Session;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TestDataHelper {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String username, String password) {
        User user = User.builder()
                .username(username)
                .password(password)
                .build();
        return userRepository.save(user);
    }

    public User createDefaultUser() {
        return createUser("testuser", "password");
    }

    public Session createSession(User user, int minutesToExpire) {
        return Session.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .expiresAt(LocalDateTime.now().plusMinutes(minutesToExpire))
                .build();
    }

    public Session createDefaultSession(User user) {
        return createSession(user, 10);
    }
}
