package com.openclassrooms.mddapi.constants;

public final class ErrorMessages {

    // User-related error messages
    public static final String EMAIL_ALREADY_IN_USE = "Email is already in use";
    public static final String USERNAME_ALREADY_IN_USE = "Username is already in use";
    public static final String INVALID_CREDENTIALS = "Invalid email or password";
    public static final String USER_NOT_FOUND = "User not found: ";

    // Authentication-related error messages
    public static final String USER_NOT_AUTHENTICATED = "User not authenticated";
    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";
    public static final String USERNAME_CANNOT_CONTAIN_AT = "Username cannot contain '@' character";
    
    public static final String UNHANDLED_EXCEPTION = "Unhandled exception caught";
    public static final String BAD_REQUEST = "Bad request: ";

    // Validation error messages (used in DTOs)
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID = "Invalid email format";
    public static final String USERNAME_REQUIRED = "Username is required";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String USERNAME_SIZE_INVALID = "Username must be between 3 and 30 characters";
    public static final String PASSWORD_INVALID = "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character";

    public static final String EMAIL_USERNAME_REQUIRED = "Email or username is required";

    private ErrorMessages() {}
}
