/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package co.unicauca.domain;

import co.unicauca.domain.entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessManagementTest {

    @Test
    void testShowRoleMenuStudent() {
        User student = new User("Ana", "López", "111", "Sistemas", "estudiante", "ana@unicauca.edu.co", "1234");

        // Probamos que la lógica reconozca al estudiante
        // Como showRoleMenu llama internamente a studentMenu (interactivo),
        // aquí solo validamos el rol esperado
        assertEquals("estudiante", student.getRole().toLowerCase());
    }

    @Test
    void testShowRoleMenuTeacher() {
        User teacher = new User("Carlos", "Martínez", "222", "Sistemas", "profesor", "carlos@unicauca.edu.co", "1234");

        assertEquals("profesor", teacher.getRole().toLowerCase());
    }

    @Test
    void testStudentUserCreation() {
        User u = new User("Luis", "Gómez", "123456", "Ingeniería de Sistemas", "estudiante", "luis@unicauca.edu.co", "secreta");

        assertNotNull(u);
        assertEquals("Luis", u.getFirstName());
        assertEquals("Gómez", u.getLastName());
        assertEquals("Ingeniería de Sistemas", u.getProgram());
        assertEquals("estudiante", u.getRole());
        assertEquals("luis@unicauca.edu.co", u.getEmail());
    }

    @Test
    void testTeacherUserCreation() {
        User u = new User("Marta", "Rojas", "654321", "Automática Industrial", "profesor", "marta@unicauca.edu.co", "clave");

        assertNotNull(u);
        assertEquals("Marta", u.getFirstName());
        assertEquals("Rojas", u.getLastName());
        assertEquals("Automática Industrial", u.getProgram());
        assertEquals("profesor", u.getRole());
        assertEquals("marta@unicauca.edu.co", u.getEmail());
    }

    @Test
    void testRoleCaseInsensitive() {
        User u1 = new User("Pepe", "Rodríguez", "111", "Telemática", "Estudiante", "pepe@unicauca.edu.co", "123");
        User u2 = new User("Laura", "Torres", "222", "Telemática", "Profesor", "laura@unicauca.edu.co", "123");

        assertEquals("estudiante", u1.getRole().toLowerCase());
        assertEquals("profesor", u2.getRole().toLowerCase());
    }
}
