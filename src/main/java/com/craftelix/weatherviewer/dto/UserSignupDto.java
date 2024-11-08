package com.craftelix.weatherviewer.dto;

import com.craftelix.weatherviewer.validation.PasswordMatches;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class UserSignupDto {

    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters long")
    @Pattern(
            regexp = "^\\S+$",
            message = "Username must not contain whitespace characters"
    )
    private String username;

    @Size(min = 3, max = 32, message = "Password must be between 3 and 32 characters long")
    @Pattern(
            regexp = "^\\S+$",
            message = "Password must not contain whitespace characters"
    )
    private String password;

    private String confirmPassword;
}
