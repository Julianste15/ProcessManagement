package co.unicauca.user.model;
import co.unicauca.user.model.enums.Career;
import co.unicauca.user.model.enums.Role;
public class UserBuilder {
    private Long id;
    private String names;
    private String surnames;
    private String email;
    private String password;
    private Long telephone;
    private Career career;
    private Role role;    
    public UserBuilder withNames(String names) {
        this.names = names;
        return this;
    }
    public UserBuilder withEmail(String email) {
        if (!isValidEmail(email)) throw new IllegalArgumentException("Email invalido");
        this.email = email;
        return this;
    }    
    public UserBuilder withPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password debe tener al menos 6 caracteres");
        }
        this.password = password;
        return this;
    }
    public User build() {
        validate();
        return new User(id, names, surnames, email, password, telephone, career, role);
    }
    private void validate() {
        if (names == null || names.trim().isEmpty()) 
            throw new IllegalStateException("Names son requeridos");
         if (email == null) {
            throw new IllegalStateException("Email es requerido");
        }
    }
    private boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
