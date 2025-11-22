package co.unicauca.infrastructure.config;
import java.util.Properties;
import java.io.InputStream;
import java.util.logging.Logger;
/**
 * Configuración para la conexión con microservicios
 */
public class MicroserviceConfig {
    private static final Logger logger = Logger.getLogger(MicroserviceConfig.class.getName());    
    private static final String DEFAULT_GATEWAY_URL = "http://localhost:8080";
    private static final String CONFIG_FILE = "microservice.properties";    
    private String gatewayUrl;    
    private static MicroserviceConfig instance;    
    private MicroserviceConfig() {
        loadConfiguration();
    }
    public static MicroserviceConfig getInstance() {
        if (instance == null) {
            instance = new MicroserviceConfig();
        }
        return instance;
    }    
    private void loadConfiguration() {
        Properties props = new Properties();        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                props.load(input);
                gatewayUrl = props.getProperty("gateway.url", DEFAULT_GATEWAY_URL);
                logger.info("Configuración cargada desde " + CONFIG_FILE);
            } else {
                gatewayUrl = DEFAULT_GATEWAY_URL;
                logger.info("Usando configuración por defecto. Gateway URL: " + gatewayUrl);
            }
        } catch (Exception e) {
            logger.warning("No se pudo cargar el archivo de configuración. Usando valores por defecto: " + e.getMessage());
            gatewayUrl = DEFAULT_GATEWAY_URL;
        }        
        String envGatewayUrl = System.getenv("GATEWAY_URL");
        if (envGatewayUrl != null && !envGatewayUrl.isEmpty()) {
            gatewayUrl = envGatewayUrl;
            logger.info("Usando Gateway URL desde variable de entorno: " + gatewayUrl);
        }
    }    
    public String getGatewayUrl() {
        return gatewayUrl;
    }    
    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
        logger.info("Gateway URL actualizado a: " + gatewayUrl);
    }
}

