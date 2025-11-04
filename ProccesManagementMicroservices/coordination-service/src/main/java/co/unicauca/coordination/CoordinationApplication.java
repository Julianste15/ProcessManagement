package co.unicauca.coordination;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CoordinationApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoordinationApplication.class, args);
    }
}