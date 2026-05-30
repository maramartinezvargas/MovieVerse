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


/**
 * CONFIGURACIÓN DE SEGURIDAD DE LA APLICACIÓN **************************************
 *
 * Define las reglas de acceso a las distintas rutas, la página de login personalizada,
 * el comportamiento tras autenticarse correctamente, el logout y la funcionalidad
 * de "remember me".
 *
 * Esta clase utiliza Spring Security para proteger las rutas privadas,
 * permitir acceso público a recursos estáticos y páginas de acceso general,
 * y restringir determinadas secciones según el rol del usuario.
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Value("${security.remember-me.key}")
    private String rememberMeKey;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configura la cadena de filtros de seguridad de Spring Security.
     *
     * Define qué rutas son públicas, cuáles requieren autenticación y cuáles
     * están restringidas a usuarios con roles concretos. También configura la
     * página de login, el comportamiento tras un inicio de sesión exitoso,
     * el cierre de sesión y la opción de "remember me".</p>
     *
     * @param http objeto de configuración HTTP de Spring Security
     * @return la cadena de filtros de seguridad construida
     * @throws Exception si ocurre un error al construir la configuración de seguridad
     */
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

                                // Reviews → usuario autenticado
                                .requestMatchers("/reviews/**")
                                .authenticated()

                                // Dashboard → solo admin/moderador
                                .requestMatchers("/dashboard/**")
                                .hasAnyRole("ADMIN", "MODERATOR")

                                // Cualquier otra ruta requiere autenticación
                                .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/"
                        })
                )

                .formLogin(form -> form
                        // Página de login personalizada
                        .loginPage("/login")
                        // URL a la que se redirige tras un error de login
                        .failureUrl("/login?error=true")
                        // Comportamiento tras un login exitoso: redirigir a la URL original o a "/"
                        .successHandler((request, response, authentication) -> {
                            // Página original a la que se intentaba acceder antes de autenticarse
                            String redirect = request.getParameter("redirect"
                            // Si no hay URL original, redirigir a la página principal
                            if (redirect != null && !redirect.isEmpty() && redirect.startsWith("/")) {
                                response.sendRedirect(redirect
                            // Sino redirigir a la raíz
                            } else {
                                response.sendRedirect("/"
                            }
                        })
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/?logoutSuccess=true")
                        .permitAll()
                )

                // "Remember Me" con configuración personalizada
                .rememberMe(remember -> remember
                        .key(rememberMeKey) // Clave secreta para tokens (configurada en application.properties)
                        .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 días
                        .userDetailsService(userDetailsService)
                

        return http.build(
    }

    /**
     * Crea el codificador de contraseñas utilizado por la aplicación.
     *
     * Se utiliza BCryptPasswordEncoder para almacenar y validar contraseñas
     * de forma segura.
     *
     * @return una instancia de PasswordEncoder basada en BCrypt
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(
    }
}