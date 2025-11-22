package co.unicauca.auth.dto;
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private String role;
    private String names;    
    private boolean requiresFormatA;
    private Long formatoAId;
    private String formatoAEstado;    
    public AuthResponse() {
        this.type = "Bearer";
        this.requiresFormatA = false;
        this.formatoAEstado = "NOT_SUBMITTED";
    }    
    public AuthResponse(String token, String email, String role, String names, String type) {
        this(token, email, role, names, type, false, null, "NOT_SUBMITTED");
    }
    public AuthResponse(String token, String email, String role, String names, String type,
                        boolean requiresFormatA, Long formatoAId, String formatoAEstado) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.names = names;
        this.type = type;
        this.requiresFormatA = requiresFormatA;
        this.formatoAId = formatoAId;
        this.formatoAEstado = formatoAEstado != null ? formatoAEstado : "NOT_SUBMITTED";
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
    public boolean isRequiresFormatA() { return requiresFormatA; }
    public void setRequiresFormatA(boolean requiresFormatA) { this.requiresFormatA = requiresFormatA; }
    public Long getFormatoAId() { return formatoAId; }
    public void setFormatoAId(Long formatoAId) { this.formatoAId = formatoAId; }
    public String getFormatoAEstado() { return formatoAEstado; }
    public void setFormatoAEstado(String formatoAEstado) { this.formatoAEstado = formatoAEstado; }
}