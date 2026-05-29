package com.mara.tfgcine.service.security;

import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio personalizado de Spring Security para cargar usuarios desde la base de datos.
 *
 * Implementa {@link UserDetailsService} para adaptar la entidad {@link com.mara.tfgcine.model.user.User}
 * al formato que necesita Spring Security durante el proceso de autenticación.
 *
 * Busca el usuario por nombre de usuario y, si existe, construye un objeto
 * {@link org.springframework.security.core.userdetails.User} con su contraseña y rol.
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see UserDetailsService
 * @see com.mara.tfgcine.model.user.User
 */
@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }


    /**
     * Carga un usuario de la aplicación a partir de su nombre de usuario.
     *
     * @param username nombre de usuario a buscar
     * @return objeto {@link UserDetails} compatible con Spring Security
     * @throws UsernameNotFoundException si el usuario no existe
    */
    @Override
    public UserDetails loadUserByUsername(
            String username
    ) {

        User user =
                userRepository.findByUsername(username

        if (user == null) {

            throw new UsernameNotFoundException(
                    "Usuario no encontrado"
            
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build(
    }
}