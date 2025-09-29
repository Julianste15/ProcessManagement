
package co.unicauca.infrastructure.dependency_injection;

import co.unicauca.infrastructure.security.Encryptor;
import org.junit.jupiter.api.Test;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class DiAnnotationAndScannerTest {
    @Test
    void serviceAnnotationIsRuntime() throws Exception {
        Retention r = Service.class.getAnnotation(Retention.class);
        assertNotNull(r);
        assertEquals(RetentionPolicy.RUNTIME, r.value());
    }
    @Test
    void scannerFindsEncryptorService() {
        ScannerPackage scan = new ScannerPackage();
        Set<Class<?>> services = scan.getClassesByAnnotation("co.unicauca.infrastructure", Service.class);
        assertTrue(services.contains(Encryptor.class));
    }
}
