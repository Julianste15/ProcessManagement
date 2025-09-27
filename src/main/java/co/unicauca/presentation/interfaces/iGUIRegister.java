package co.unicauca.presentation.interfaces;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
public interface iGUIRegister {
    JFrame getView();   
    JTextField getFieldName(); 
    JTextField getFieldSurname(); 
    JTextField getFieldEmail(); 
    JPasswordField getFieldPassword();  
    JTextField getFieldPhone();  
    JComboBox<String> getFieldCareer();  
    JComboBox<String> getFieldRole(); 
    JButton getButtonRegister(); 
    JButton getButtonBackLogin();
    void showMessage(String prmMessage, int prmMessageType);
}
