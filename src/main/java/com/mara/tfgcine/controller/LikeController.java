package com.mara.tfgcine.controller;

import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.service.LikeService;
import com.mara.tfgcine.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * CONTROLADOR ENCARGADO DE GESTIONAR LOS "LIKES" (Me gusta) DE LOS USUARIOS DE MOVIEVERSE
 *
 * Expone endpoints para alternar el estado de un like sobre un contenido
 * multimedia y devolver el estado actualizado junto con el total de likes.
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */
@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;
    private final UserService userService;

    public LikeController(LikeService likeService, UserService userService) {
        this.likeService = likeService;
        this.userService = userService;
    }


    /**
     * Alterna el estado de "like" de un usuario sobre un contenido multimedia.
     *
     * Si el usuario no está autenticado, devuelve un mensaje de error.
     * En caso contrario, obtiene el usuario autenticado, invoca el servicio de likes
     * y devuelve si el contenido quedó marcado o no, junto con el número total de likes.
     *
     * @param mediaId identificador del contenido multimedia
     * @param mediaType tipo de contenido multimedia
     * @param principal información de autenticación del usuario actual
     * @return un mapa con el estado del like y el total de likes, o un error si no hay usuario autenticado
     */
    @PostMapping("/toggle")
    public Map<String, Object> toggleLike(
            @RequestParam Long mediaId,
            @RequestParam MediaType mediaType,
            Principal principal
    ) {

        // Validar autenticación del usuario
        if (principal == null) {
            return Map.of("error", "Usuario no autenticado"
        }

        User user = userService.findByUsername(principal.getName()

        boolean liked = likeService.toggleLike(user, mediaId, mediaType
        int totalLikes = likeService.countLikes(mediaId, mediaType

        return Map.of(
                "liked", liked,
                "totalLikes", totalLikes
        
    }
}