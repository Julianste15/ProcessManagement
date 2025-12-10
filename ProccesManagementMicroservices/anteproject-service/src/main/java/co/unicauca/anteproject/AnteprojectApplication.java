package co.unicauca.anteproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import co.unicauca.anteproject.domain.ports.out.AnteprojectRepositoryPort;
import co.unicauca.anteproject.infrastructure.output.persistence.AnteprojectPersistenceAdapter;
import co.unicauca.anteproject.infrastructure.output.persistence.repository.JpaAnteprojectRepository;
import co.unicauca.anteproject.infrastructure.output.persistence.mapper.AnteprojectMapper;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@org.springframework.context.annotation.ComponentScan(basePackages = "co.unicauca.anteproject")
public class AnteprojectApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnteprojectApplication.class, args);
    }

    @Bean
    public AnteprojectRepositoryPort anteprojectRepositoryPort(JpaAnteprojectRepository jpaRepository,
            AnteprojectMapper mapper) {
        return new AnteprojectPersistenceAdapter(jpaRepository, mapper);
    }
}