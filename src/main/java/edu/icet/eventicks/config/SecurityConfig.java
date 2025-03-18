package edu.icet.eventicks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**").permitAll()
//                        .anyRequest().authenticated())
//                .build();

                .csrf(csrf -> csrf.disable()) // Disable CSRF for development only
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Allow all requests temporarily
                )
                .build();
    }
}
