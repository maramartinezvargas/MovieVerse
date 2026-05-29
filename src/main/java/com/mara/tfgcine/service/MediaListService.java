package com.mara.tfgcine.service;

import com.mara.tfgcine.model.list.MediaList;
import com.mara.tfgcine.repository.MediaListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* Servicio encargado de gestionar las listas personalizadas de medios de un usuario.
*
* Actualmente proporciona acceso de lectura a las listas del usuario, cargando también
 * sus elementos para mostrarlos en el perfil sin problemas de lazy loading.
*
* @author Tamara Martinez Vargas
 * @since 02/03/2026
* @version 28/05/2026
* @see MediaList
 * @see MediaListRepository
 */
@Service
public class MediaListService {

    private final MediaListRepository mediaListRepository;

    public MediaListService(MediaListRepository mediaListRepository) {
        this.mediaListRepository = mediaListRepository;
    }

    /**
     * Obtiene todas las listas de medios de un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario del propietario de las listas
     * @return lista de `MediaList` asociadas al usuario
     */
    public List<MediaList> getListsByUsername(String username) {
        return mediaListRepository.findWithItemsByUsername(username
    }
}