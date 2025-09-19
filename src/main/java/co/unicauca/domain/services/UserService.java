package co.unicauca.domain.services;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.repositories.IUserRepositoriy;
import java.util.Optional;

public class UserService {
    private final IUserRepositoriy repository;
    private final PasswordValidator passwordValidator;
    private final PasswordEncryptor passwordEncryptor;
    private final EmailValidator emailValidator;

    public UserService(IUserRepositoriy repository, PasswordValidator passwordValidator, PasswordEncryptor passwordEncryptor, EmailValidator emailValidator) {
        this.repository = repository;
        this.passwordValidator = passwordValidator;
        this.passwordEncryptor = passwordEncryptor;
        this.emailValidator = emailValidator;
    }
    
    public void registerUser(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (!emailValidator.isInstitutional(user.getEmail()))
            throw new IllegalArgumentException("Email must be @unicauca.edu.co");
        if (!passwordValidator.isValid(user.getPassword()))
            throw new IllegalArgumentException("Weak password");

        if (repository.existsByEmail(user.getEmail()))
            throw new IllegalArgumentException("Email already registered");

        user.setPassword(passwordEncryptor.encrypt(user.getPassword()));
        repository.save(user);
    }
    
    public Optional<User> login(String email, String rawPassword) {
        Optional<User> userOpt = repository.findByEmail(email);
        if (userOpt.isEmpty()) return Optional.empty();

        User user = userOpt.get();
        boolean ok = passwordEncryptor.matches(rawPassword, user.getPassword());
        return ok ? Optional.of(user) : Optional.empty();
    }
  
}