package org.zeta.validation;

import org.zeta.model.enums.Role;

import java.util.regex.Pattern;

public class UserValidator {

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[A-Za-z][A-Za-z0-9_]{2,19}$");

    public static void validateRegistration(String username,
                                            String password,
                                            String confirmPassword,
                                            Role role) {

        CommonValidator.validateNotNullOrEmpty(username, "Username");
        CommonValidator.validateNotNullOrEmpty(password, "Password");

        CommonValidator.validateMinLength(username, 3, "Username");
        CommonValidator.validateMinLength(password, 6, "Password");

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new ValidationException("""
                    Invalid username format:
                    - Must start with a letter
                    - Can contain letters, numbers, underscore
                    - Length must be 3 to 20 characters
                    - No spaces or special characters
                    """);
        }

        if (!password.equals(confirmPassword)) {
            throw new ValidationException("Passwords do not match.");
        }

        if (role == null) {
            throw new ValidationException("Role must be selected.");
        }
    }

    public static void validateLogin(String username, String password) {

        CommonValidator.validateNotNullOrEmpty(username, "Username");
        CommonValidator.validateNotNullOrEmpty(password, "Password");
    }
}
