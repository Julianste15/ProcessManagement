package co.unicauca.auth.dto;
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private String role;
    private String names;    
    public AuthResponse() {}    
    public AuthResponse(String token, String email, String role, String names, String type) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.names = names;
        this.type = type;
    }    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }    
    public String getNames() { return names; }
    public void setNames(String names) { this.names = names; }
}