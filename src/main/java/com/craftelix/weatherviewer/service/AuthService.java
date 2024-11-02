package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.dto.UserDto;
import com.craftelix.weatherviewer.dto.UserLoginDto;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.exception.InvalidPasswordException;
import com.craftelix.weatherviewer.exception.UserNotFoundException;
import com.craftelix.weatherviewer.mapper.UserMapper;
import com.craftelix.weatherviewer.repository.UserRepository;
import com.craftelix.weatherviewer.util.PasswordHashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserDto authenticate(UserLoginDto userLoginDto) {
        User user = userRepository.findByUsername(userLoginDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", userLoginDto.getUsername())));

        if (!PasswordHashing.checkPassword(userLoginDto.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        return userMapper.toDto(user);
    }

}
