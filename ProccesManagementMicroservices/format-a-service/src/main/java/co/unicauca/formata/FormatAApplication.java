package co.unicauca.formata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "co.unicauca.formata.client")
public class FormatAApplication {
    public static void main(String[] args) {
        SpringApplication.run(FormatAApplication.class, args);
    }   
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}