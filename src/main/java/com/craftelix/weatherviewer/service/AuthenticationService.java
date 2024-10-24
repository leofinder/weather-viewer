package com.craftelix.weatherviewer.service;

import com.craftelix.weatherviewer.dto.UserDto;
import com.craftelix.weatherviewer.entity.User;
import com.craftelix.weatherviewer.exception.InvalidPasswordException;
import com.craftelix.weatherviewer.exception.UserNotFoundException;
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
public class AuthenticationService {

    private final UserRepository userRepository;

    public User authenticate(UserDto userDto) {
        User user = userRepository.findByLogin(userDto.getLogin())
                .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", userDto.getLogin())));

        if (!PasswordHashing.checkPassword(userDto.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        return user;
    }

}
