package co.unicauca.user.dto;
import co.unicauca.user.model.enums.Career;
import co.unicauca.user.model.enums.Role;
public class UserDTO {
    private Long id;
    private String names;
    private String surnames;
    private String email;
    private Long telephone;
    private Career career;
    private Role role;
    public UserDTO() {}
    public UserDTO(Long id, String names, String surnames, String email, 
                   Long telephone, Career career, Role role) {
        this.id = id;
        this.names = names;
        this.surnames = surnames;
        this.email = email;
        this.telephone = telephone;
        this.career = career;
        this.role = role;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNames() { return names; }
    public void setNames(String names) { this.names = names; }  
    public String getSurnames() { return surnames; }
    public void setSurnames(String surnames) { this.surnames = surnames; }  
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }    
    public Long getTelephone() { return telephone; }
    public void setTelephone(Long telephone) { this.telephone = telephone; }    
    public Career getCareer() { return career; }
    public void setCareer(Career career) { this.career = career; }    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}