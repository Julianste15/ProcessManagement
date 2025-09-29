
package co.unicauca.presentation.controllers;

import co.unicauca.infrastructure.dependency_injection.Controller;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ControllerAnnotationTest {
    @Test
    void loginControllerIsAnnotated() {
        assertNotNull(GUILoginController.class.getAnnotation(Controller.class));
    }
}
