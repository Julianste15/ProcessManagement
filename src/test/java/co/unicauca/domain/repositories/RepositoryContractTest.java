
package co.unicauca.domain.repositories;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class RepositoryContractTest {
    @Test
    void userRepositoryHasCrudMethods() throws Exception {
        Class<?> c = UserRepository.class;
        String[] expected = {"save","findByEmail","findById","delete","findAll"};
        for (String m : expected) {
            Method found = null;
            for (Method method : c.getDeclaredMethods()) {
                if (method.getName().equals(m)) { found = method; break; }
            }
            assertNotNull(found, "Missing method: " + m);
        }
    }
}
