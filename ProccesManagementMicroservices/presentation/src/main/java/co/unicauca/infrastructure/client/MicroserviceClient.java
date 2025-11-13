package co.unicauca.infrastructure.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Cliente HTTP para comunicarse con los microservicios a través del Gateway
 */
public class MicroserviceClient {
    private static final Logger logger = Logger.getLogger(MicroserviceClient.class.getName());
    
    private final String gatewayBaseUrl;
    private final ObjectMapper objectMapper;
    private String currentToken;
    
    public MicroserviceClient(String gatewayBaseUrl) {
        this.gatewayBaseUrl = gatewayBaseUrl.endsWith("/") 
            ? gatewayBaseUrl.substring(0, gatewayBaseUrl.length() - 1) 
            : gatewayBaseUrl;
        this.objectMapper = new ObjectMapper();
        this.currentToken = null;
    }
    
    public void setToken(String token) {
        this.currentToken = token;
    }
    
    public String getToken() {
        return currentToken;
    }
    
    public void clearToken() {
        this.currentToken = null;
    }
    
    public <T> T post(String endpoint, Object requestBody, Class<T> responseType) throws Exception {
        String url = gatewayBaseUrl + endpoint;
        logger.info("POST a: " + url);
        
        HttpURLConnection connection = createConnection(url, "POST");
        
        if (requestBody != null) {
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }
        
        return handleResponse(connection, responseType);
    }
    
    public <T> T get(String endpoint, Class<T> responseType) throws Exception {
        String url = gatewayBaseUrl + endpoint;
        logger.info("GET a: " + url);
        
        HttpURLConnection connection = createConnection(url, "GET");
        return handleResponse(connection, responseType);
    }
    
    private HttpURLConnection createConnection(String urlString, String method) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        
        if (currentToken != null && !currentToken.isEmpty()) {
            connection.setRequestProperty("Authorization", "Bearer " + currentToken);
        }
        
        if ("POST".equals(method) || "PUT".equals(method)) {
            connection.setDoOutput(true);
        }
        
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);
        
        return connection;
    }
    
    @SuppressWarnings("unchecked")
    private <T> T handleResponse(HttpURLConnection connection, Class<T> responseType) throws Exception {
        int responseCode = connection.getResponseCode();
        
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                responseCode >= 200 && responseCode < 300 
                    ? connection.getInputStream() 
                    : connection.getErrorStream(),
                StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        
        String responseBody = response.toString();
        logger.info("Respuesta (" + responseCode + "): " + responseBody);
        
        if (responseCode >= 200 && responseCode < 300) {
            if (responseType == String.class) {
                return (T) responseBody;
            } else if (responseType == Map.class || responseType == Object.class) {
                return (T) objectMapper.readValue(responseBody, Map.class);
            } else {
                return objectMapper.readValue(responseBody, responseType);
            }
        } else {
            String errorMessage = "Error HTTP " + responseCode;
            try {
                Map<String, Object> errorMap = objectMapper.readValue(responseBody, Map.class);
                if (errorMap.containsKey("message")) {
                    errorMessage = (String) errorMap.get("message");
                } else if (errorMap.containsKey("error")) {
                    errorMessage = (String) errorMap.get("error");
                }
            } catch (Exception e) {
                if (!responseBody.isEmpty()) {
                    errorMessage = responseBody;
                }
            }
            throw new RuntimeException(errorMessage);
        }
    }
}

