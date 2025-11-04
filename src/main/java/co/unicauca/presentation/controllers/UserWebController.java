    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.presentation.controllers;

import co.unicauca.domain.entities.User;
import co.unicauca.domain.exceptions.UserException;
import co.unicauca.domain.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// --- Anotaciones Clave ---
@RestController // 1. Le dice a Spring que esto es un controlador API REST (no GUI)
@RequestMapping("/api/users") // 2. Todas las rutas en esta clase empezarán con /api/users
public class UserWebController {

    // 3. Inyectamos el MISMO servicio que usa tu GUIController
    @Autowired
    private UserService userService;

    // 4. Esta es la ruta que tu React está buscando: /api/users/register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User userData) {
        
        try {
            // 5. Llamamos a la lógica de negocio que YA EXISTE
            User registeredUser = userService.registerUser(userData);
            
            // 6. Si tiene éxito, devolvemos 200 OK.
            // (La respuesta está vacía, lo cual nuestro api.js ya sabe manejar)
            return ResponseEntity.ok().build();

        } catch (UserException ex) {
            // 7. ¡AQUÍ ESTÁ LA MAGIA!
            // Si el usuario ya existe, tu servicio lanza UserException.
            // La capturamos y devolvemos un error 400 (Bad Request)
            // con el mensaje de error EXACTO que tu backend definió (ej: "El correo ya existe").
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
                    
        } catch (Exception ex) {
            // 8. Capturamos cualquier otro error inesperado
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Un error inesperado ocurrió: " + ex.getMessage());
        }
    }
}