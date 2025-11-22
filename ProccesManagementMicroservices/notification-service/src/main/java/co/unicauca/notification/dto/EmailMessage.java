package co.unicauca.notification.dto;
public class EmailMessage {
    private String to;
    private String subject;
    private String message;
    private boolean isHtml = false;    
    public EmailMessage() {}    
    public EmailMessage(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }    
    public EmailMessage(String to, String subject, String message, boolean isHtml) {
        this.to = to;
        this.subject = subject;
        this.message = message;
        this.isHtml = isHtml;
    }    
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }    
    public boolean isHtml() { return isHtml; }
    public void setHtml(boolean html) { isHtml = html; }
}