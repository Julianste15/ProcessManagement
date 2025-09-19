package co.unicauca.domain.entities;

public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String program;
    private String role;
    private String email;
    private String password;

    public User(String firstName, String lastName, String phone, String program, String role, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.program = program;
        this.role = role;
        this.email = email;
        this.password = password;
    }
    
    public User(Long id, String firstName, String lastName, String phone, String program, String role, String email, String password) {
        this(firstName, lastName, phone, program, role, email, password);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
