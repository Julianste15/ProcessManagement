package co.unicauca.domain.entities;

import co.unicauca.domain.enums.Role;
import co.unicauca.domain.enums.Career;

public class User {
    private Long id;
    private String names;
    private String surnames;
    private String email;
    private String password;
    private Long telephone;
    private Career career;
    private Role role;
    
    public User() {}
    
    public User(Long id, String names, String surnames, String email, 
                String password, Long telephone, Career career, Role role) {
        this.id = id;
        this.names = names;
        this.surnames = surnames;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.career = career;
        this.role = role;
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNames() { return names; }
    public void setNames(String names) { this.names = names; }
    
    public String getSurnames() { return surnames; }
    public void setSurnames(String surnames) { this.surnames = surnames; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Long getTelephone() { return telephone; }
    public void setTelephone(Long telephone) { this.telephone = telephone; }
    
    public Career getCareer() { return career; }
    public void setCareer(Career career) { this.career = career; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public String getFullName() {
        return (names != null ? names : "") + " " + (surnames != null ? surnames : "");
    }
}

