/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Deshabilitar CSRF (no es necesario para una API REST stateless)
            .csrf(csrf -> csrf.disable())

            // 2. Configurar CORS para permitir peticiones desde tu frontend
            .cors(cors -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(Arrays.asList("*"));
                config.setAllowCredentials(true);
                
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                cors.configurationSource(source);
            })

            // 3. Configurar la autorización de peticiones
            .authorizeHttpRequests(authz -> authz
                // --- INICIO DE LA CORRECCIÓN ---
                // 3a. PERMITIR el acceso público a login y registro
                .requestMatchers("/api/auth/login", "/api/users/register").permitAll()
                
                // 3b. REQUERIR autenticación para todo lo demás
                .anyRequest().authenticated()
                // --- FIN DE LA CORRECCIÓN ---
            )

            // 4. Configurar la gestión de sesión como STATELESS (para API REST)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}