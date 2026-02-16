package org.zeta.validation;

import org.zeta.model.Role;

public class UserValidator {

    public static void validateRegistration(String username,
                                            String password,
                                            String confirmPassword,
                                            Role role) {

        CommonValidator.validateNotNullOrEmpty(username, "Username");
        CommonValidator.validateNotNullOrEmpty(password, "Password");

        CommonValidator.validateMinLength(username, 3, "Username");
        CommonValidator.validateMinLength(password, 6, "Password");

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
