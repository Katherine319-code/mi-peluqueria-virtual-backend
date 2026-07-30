package com.peluqueria.mipeluqueriavirtual.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ── Publico: login, registro, google, recuperacion de contrasena ──
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/estilistas/login").permitAll()

                // ── Catalogo de servicios: lectura publica ──
                .requestMatchers(HttpMethod.GET, "/api/servicios/**").permitAll()

                // ── Solo ADMIN crea/edita/desactiva servicios ──
                .requestMatchers(HttpMethod.POST, "/api/servicios").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/servicios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/servicios/**").hasRole("ADMIN")

                // ── Solo ADMIN crea/desactiva estilistas ──
                .requestMatchers(HttpMethod.POST, "/api/estilistas").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/estilistas/*/desactivar").hasRole("ADMIN")

                // ── Solo ADMIN ve el listado completo de citas o borra citas ──
                .requestMatchers(HttpMethod.GET, "/api/citas").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/citas/**").hasRole("ADMIN")

                // ── Solo ADMIN administra usuarios en bloque ──
                .requestMatchers(HttpMethod.GET, "/api/usuarios").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMIN")

                // ── Todo lo demas requiere estar logueado (cualquier rol) ──
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}