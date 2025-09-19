package co.unicauca.domain.services;

import java.util.regex.Pattern;

public class PasswordValidator {
    private static final int MIN_LENGTH = 6;
    
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(".*[!@#$%^&*(),.?\":{}|<>].*");

    public boolean isValid(String password){
        if (password == null) return false;
        
        return password.length() >= MIN_LENGTH
                && DIGIT_PATTERN.matcher(password).matches()
                && UPPERCASE_PATTERN.matcher(password).matches()
                && SPECIAL_CHAR_PATTERN.matcher(password).matches();
    }
}
