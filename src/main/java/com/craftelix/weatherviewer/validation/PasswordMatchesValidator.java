package com.craftelix.weatherviewer.validation;

import com.craftelix.weatherviewer.dto.user.UserSignupDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserSignupDto> {

    @Override
    public boolean isValid(UserSignupDto user, ConstraintValidatorContext context) {
        return user.getPassword() != null && user.getPassword().equals(user.getConfirmPassword());
    }
}