package org.zeta.validation;

public class CommonValidator {

    public static void validateNotNullOrEmpty(String value, String fieldName) {

        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty.");
        }
    }

    public static void validateMinLength(String value, int minLength, String fieldName) {

        if (value.length() < minLength) {
            throw new ValidationException(
                    fieldName + " must be at least " + minLength + " characters.");
        }
    }

    public static void validateUUID(String value, String fieldName) {

        try {
            java.util.UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(fieldName + " is not a valid UUID.");
        }
    }
    public static int validateInteger(String input, String fieldName) {

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName + " must be a valid number.");
        }
    }

}
