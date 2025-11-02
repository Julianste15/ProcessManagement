package co.unicauca.presentation.interfaces;
import co.unicauca.presentation.views.GUIRegister;
public interface iGUIRegisterController {
    void run();
    void register(GUIRegister prmGUIRegister);
    void backToLogin(GUIRegister prmGUIRegister);
}
