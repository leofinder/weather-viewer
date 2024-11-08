package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.dto.UserDto;
import com.craftelix.weatherviewer.dto.UserSignupDto;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.exception.SessionNotFoundException;
import com.craftelix.weatherviewer.exception.UserAlreadyExistException;
import com.craftelix.weatherviewer.mapper.UserMapper;
import com.craftelix.weatherviewer.repository.UserRepository;
import com.craftelix.weatherviewer.util.PasswordHashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public void save(UserSignupDto userSignupDto) {
        User user = userMapper.toEntity(userSignupDto);
        user.setPassword(PasswordHashing.hashPassword(user.getPassword()));

        try {
            userRepository.save(user);
        } catch (DbActionExecutionException e) {
            if (e.getCause() instanceof DuplicateKeyException) {
                throw new UserAlreadyExistException(String.format("User %s already exists", userSignupDto.getUsername()));
            }
            throw new RuntimeException(e);
        }

    }

    public UserDto getUserBySessionId(UUID sessionId) {
        User user = userRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(String.format("User not found by session id %s", sessionId)));
        return userMapper.toDto(user);
    }
}
