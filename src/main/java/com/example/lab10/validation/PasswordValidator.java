package com.example.lab10.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        
        // Password must be at least 8 characters
        if (password.length() < 8) {
            return false;
        }
        
        // Check for at least one uppercase letter
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        // Check for at least one lowercase letter
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        // Check for at least one digit
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        return hasUpperCase && hasLowerCase && hasDigit;
    }
}
