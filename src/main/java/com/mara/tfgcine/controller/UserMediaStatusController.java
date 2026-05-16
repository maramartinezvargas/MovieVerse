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
    public ResponseEntity<Map<String, Object>> saveStatus(@RequestBody StatusRequestDTO request, Authentication authentication) {

        Map<String, Object> response =
                new HashMap<>(

        /* Usuario no autenticado */
        if (authentication == null || !authentication.isAuthenticated()) {

            response.put("success", false

            response.put("message", "Usuario no autenticado"

            return ResponseEntity
                    .status(401)
                    .body(response

        }

        /* Obtener usuario */
        User user = userService.findByUsername(authentication.getName()

        /* Guardar estado */
        UserMediaStatus savedStatus = userMediaStatusService.saveStatus(
                        user,
                        request.getMediaId(),
                        request.getMediaType(),
                        request.getStatus()
                

        response.put("success", true

        /* Si es null → toggle de estado, se ha eliminado el registro */
        if (savedStatus == null) {
            response.put("status", null
        } else {
            response.put("status", savedStatus.getStatus().name()
        }

        return ResponseEntity.ok(response
    }
}