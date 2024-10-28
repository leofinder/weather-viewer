package com.craftelix.weatherviewer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupDto {

    private String login;

    private String password;

    private String confirmPassword;
}
