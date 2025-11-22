package co.unicauca.user.model;
import co.unicauca.user.model.enums.Career;
import co.unicauca.user.model.enums.Role;
import jakarta.persistence.*;
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "names", nullable = false)
    private String names;
    @Column(name = "surnames", nullable = false)
    private String surnames;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "telephone")
    private Long telephone;
    @Enumerated(EnumType.STRING)
    @Column(name = "career", nullable = false)
    private Career career;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
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
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getNames() {return names;}
    public void setNames(String names) {this.names = names;}
    public String getSurnames() {return surnames;}
    public void setSurnames(String surnames) {this.surnames = surnames;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public Long getTelephone() {return telephone;}
    public void setTelephone(Long telephone) {this.telephone = telephone;}
    public Career getCareer() {return career;}
    public void setCareer(Career career) {this.career = career;}
    public Role getRole() {return role;}
    public void setRole(Role role) {this.role = role;}
    public boolean isTeacher() {return Role.TEACHER.equals(this.role);}
    public boolean isStudent() {return Role.STUDENT.equals(this.role);}
    public boolean isCoordinator() {return Role.COORDINATOR.equals(this.role);}
    public boolean canAccessCareer(Career otherCareer) {return this.career.equals(otherCareer);}
}