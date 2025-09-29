
package co.unicauca.domain.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnumsSmokeTest {
    @Test
    void roleHelpersWork() {
        assertTrue(Role.STUDENT.isStudent());
        assertTrue(Role.TEACHER.isTeacher());
        assertTrue(Role.ADMINISTRATOR.isAdministrator());
    }
    @Test
    void careerFromDisplayAndDbValue() {
        assertEquals(Career.SYSTEMS_ENGINEERING, Career.fromDisplayName("Ingeniería de Sistemas"));
        assertEquals(Career.SYSTEMS_ENGINEERING, Career.fromDatabaseValue("SYSTEMS_ENGINEERING"));
    }
}
