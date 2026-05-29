
package com.mara.tfgcine.controller;
import com.mara.tfgcine.model.media.MediaType;
import com.mara.tfgcine.model.status.MediaStatus;
import com.mara.tfgcine.model.status.UserMediaStatus;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.service.UserMediaStatusService;
import com.mara.tfgcine.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.mara.tfgcine.model.dto.StatusRequestDTO;

import java.util.HashMap;
import java.util.Map;


/**
 * Controlador REST encargado de gestionar el estado de visualización del contenido multimedia del usuario.
 *
 * Permite guardar o actualizar el estado de una película o serie para un usuario autenticado,
 * devolviendo una respuesta JSON con el resultado de la operación.
 *
 * @author Tamara Martínez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */
@RestController
@RequestMapping("/status")
public class UserMediaStatusController {

    private final UserMediaStatusService
            userMediaStatusService;

    private final UserService userService;

    public UserMediaStatusController(UserMediaStatusService userMediaStatusService, UserService userService)
    {
        this.userMediaStatusService = userMediaStatusService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> saveStatus(
        @RequestBody StatusRequestDTO request,
        Authentication authentication) {

        Map<String, Object> response = new HashMap<>(

        // Validación de autenticación
        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("success", false
            response.put("message", "Usuario no autenticado"
            return ResponseEntity
                    .status(401)
                    .body(response
        }

        // Obtener usuario autenticado
        User user = userService.findByUsername(authentication.getName()

        // Guardar o actualizar el estado de visualización del usuario para el contenido multimedia
        UserMediaStatus savedStatus =
                userMediaStatusService.saveStatus(
                        user,
                        request.getMediaId(),
                        request.getMediaType(),
                        request.getTitle(),
                        request.getPosterPath(),
                        request.getVoteAverage(),
                        request.getStatus()
                

        response.put("success", true

        // Devolver el estado guardado en la respuesta (toggle de estado:
        // Enum MediaStatus: WATCHED o WATCHLIST (vista o pendiente de ver)
        if (savedStatus == null) {
            response.put("status", null
        } else {
            response.put(
                    "status",
                    savedStatus.getStatus().name()
            
        }
        return ResponseEntity.ok(response
    }
}