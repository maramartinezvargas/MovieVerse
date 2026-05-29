package com.mara.tfgcine.repository;
import com.mara.tfgcine.model.list.MediaList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * CÓDIGO LISTO PARA FUTURA MEJORA: La sección "Mis listas personalizadas" está deshabilitada
 * en profile.html. Falta implementar la creación de listas, la edición e implementar la UI.
 *
 * Repositorio Spring Data JPA para acceder a listas de medios de usuarios.
 *
 * Proporciona operaciones CRUD y consultas personalizadas sobre la entidad {@link com.mara.tfgcine.model.list.MediaList}.
 * Se utiliza para gestionar las listas personalizadas creadas por usuarios para organizar películas y series.
 *
 * Métodos personalizados:
 * - findWithItemsByUsername(): obtiene todas las listas de un usuario con sus elementos precargados (LEFT JOIN FETCH)
 *   para evitar problemas de lazy loading
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.list.MediaList
 * @see com.mara.tfgcine.model.list.ListItem
 */
public interface MediaListRepository extends JpaRepository<MediaList, Long> {

    @Query("SELECT l FROM MediaList l LEFT JOIN FETCH l.items WHERE l.user.username = :username")
    List<MediaList> findWithItemsByUsername(@Param("username") String username
}