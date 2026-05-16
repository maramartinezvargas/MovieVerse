package com.mara.tfgcine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Value("${security.remember-me.key}")
    private String rememberMeKey;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {



        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",

                                // estáticos
                                "/css/**",
                                "/js/**",
                                "/img/**",

                                // APIs públicas
                                "/api/**",
                                "/api/search",
                                "/api/peliculas",

                                // contenido público
                                "/peliculas",
                                "/peliculas/**",
                                "/series",
                                "/series/**"
                        ).permitAll()

                        // Rutas protegidas (solo para usuarios autenticados)
                        .requestMatchers("/reviews/**").authenticated()

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        // Personalizar la página de login
                        .loginPage("/login")

                        // Personalizar el comportamiento después del login exitoso
                        .successHandler((request, response, authentication) -> {

                            String redirect = request.getParameter("redirect"

                            if (redirect != null && !redirect.isEmpty() && redirect.startsWith("/")) {
                                response.sendRedirect(redirect
                            } else {
                                response.sendRedirect("/"
                            }

                        })

                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )

                // "Remember Me" con configuración personalizada
                .rememberMe(remember -> remember
                        .key(rememberMeKey) // Clave secreta para tokens (configurada en application.properties)
                        .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 días
                        .userDetailsService(userDetailsService)
                

        return http.build(
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(
    }
}