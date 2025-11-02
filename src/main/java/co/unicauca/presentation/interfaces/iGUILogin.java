package co.unicauca.presentation.interfaces;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
public interface iGUILogin {  
    JFrame getView();  
    JTextField getFieldEmail();   
    JPasswordField getFieldPassword(); 
    JButton getButtonRegister();  
    JButton getButtonLogin();
    void showMessage(String prmMessage, int prmMessageType);
}
