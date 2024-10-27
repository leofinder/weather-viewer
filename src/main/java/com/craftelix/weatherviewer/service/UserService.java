package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.dto.UserCreateDto;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.exception.PasswordMismatchException;
import com.craftelix.weatherviewer.exception.SessionNotFoundException;
import com.craftelix.weatherviewer.exception.UserAlreadyExistException;
import com.craftelix.weatherviewer.mapper.UserMapper;
import com.craftelix.weatherviewer.repository.UserRepository;
import com.craftelix.weatherviewer.util.PasswordHashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public void save(UserCreateDto userCreateDto) {
        User user = userMapper.toEntity(userCreateDto);
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new UserAlreadyExistException(String.format("User %s already exists", user.getLogin()));
        } else if (!userCreateDto.getPassword().equals(userCreateDto.getConfirmPassword())) {
            throw new PasswordMismatchException("Password and confirm password do not match");
        }
        user.setPassword(PasswordHashing.hashPassword(user.getPassword()));
        userRepository.save(user);
    }

    public User getUserBySessionId(UUID sessionId) {
        return userRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(String.format("User not found by session id %s", sessionId)));
    }
}
