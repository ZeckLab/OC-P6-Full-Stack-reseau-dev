package com.openclassrooms.mddapi.constants;

public class AppMessages {

    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String USERNAME_REGEX = "^[^@]+$";

    private AppMessages() {}
}
