package com.mara.tfgcine.repository;

import com.mara.tfgcine.model.list.ListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para acceder a elementos de listas de medios.
 *
 * Proporciona operaciones CRUD y consultas personalizadas sobre la entidad {@link com.mara.tfgcine.model.list.ListItem}.
 * Se utiliza para gestionar los contenidos (películas/series) agregados a las listas creadas por usuarios.
 *
 * Métodos personalizados:
 * - findByListId(): obtiene todos los elementos pertenecientes a una lista específica
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 * @see com.mara.tfgcine.model.list.ListItem
 * @see com.mara.tfgcine.model.list.MediaList
 */
public interface ListItemRepository extends JpaRepository<ListItem, Long> {

    List<ListItem> findByListId(Long listId

}