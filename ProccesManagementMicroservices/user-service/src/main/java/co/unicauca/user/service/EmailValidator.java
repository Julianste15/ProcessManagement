package co.unicauca.user.service;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
@Component
public class EmailValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@unicauca\\.edu\\.co$");
    public boolean isInstitutional(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}
