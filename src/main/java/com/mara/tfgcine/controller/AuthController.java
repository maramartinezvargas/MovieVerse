package com.mara.tfgcine.controller;
import com.mara.tfgcine.model.user.User;
import com.mara.tfgcine.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * CONTROLADOR DE AUTENTICACIÓN Y REGISTRO DE USUARIOS *******************************
 *
 * Gestiona las vistas de inicio de sesión y registro, además de procesar
 * el formulario de alta de nuevos usuarios validando que las contraseñas coincidan
 * y delegando el registro al servicio de usuarios.
 *
 * @author Tamara Martínez Vargas
 * @since 02/05/2026
 * @version 28/05/2026
 */

@Controller
public class AuthController {
    private final UserService userService;

    /**
     * Constructor que inyecta el servicio de usuarios.
     * @param userService el servicio de usuarios para gestionar el registro y autenticación
     *
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Muestra la página de inicio de sesión.
     *
     * @return el nombre de la vista de login
     */
    @GetMapping("/login")
    public String login() {
        return "login"; // login.html
    }

    /**
     * Formulario de registro de usuario.
     *
     * Inicializa un objeto `User` vacío para enlazarlo al formulario.
     *
     * @param model modelo de Spring para enviar atributos a la vista
     * @return el nombre de la vista de registro
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User()
        return "register";
    }

    /**
     * Procesa el registro de un nuevo usuario.
     *
     * Primero verifica que la contraseña y su confirmación coincidan.
     * Si son diferentes, devuelve el formulario con un mensaje de error.
     * Si coinciden, intenta registrar al usuario mediante `UserService`.
     *
     * @param user objeto con los datos del usuario introducidos en el formulario
     * @param confirmPassword confirmación de la contraseña
     * @param model modelo de Spring para enviar mensajes a la vista
     * @return redirección a la página de login si el registro es correcto, o la vista de registro si hay error
     */
    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam String confirmPassword,
                           Model model) {

        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden"
            return "register";
        }

        try {
            userService.register(user
            return "redirect:/login?registered=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage()
            return "register";
        }
    }
}