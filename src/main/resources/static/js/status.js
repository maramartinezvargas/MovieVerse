/**
 * status.js
 *
 * Gestiona el estado de visualización de películas/series (WATCHED, WATCHLIST, o sin estado).
 * - Dropdown personalizado para seleccionar estado
 * - POST a /status con snapshot data (title, posterPath, voteAverage)
 * - Actualiza el botón principal según el estado guardado
 * - Muestra mensajes flash de error
 */
document.addEventListener("DOMContentLoaded", () => {
    const csrfInput = document.querySelector("input[name='_csrf']"

    const csrfToken = csrfInput ? csrfInput.value : null;
    const isAuthenticated = document.body.dataset.authenticated === "true";

    const statusWrappers =
        document.querySelectorAll(".media-status-wrapper"

    statusWrappers.forEach(wrapper => {

        const button =
            wrapper.querySelector(".btn-status"

        const dropdown =
            wrapper.querySelector(".status-dropdown"

        const options =
            wrapper.querySelectorAll(".status-option"

        if (!button || !dropdown) return;

        /* Abrir / cerrar dropdown - Solo para usuarios autenticados --------------*/
        button.addEventListener("click", (e) => {

            e.stopPropagation(

            // Si usuario no autenticado, se muestra aviso y no se abre el dropdown
            if (!isAuthenticated) {
                showFlashMessage(
                    "error",
                    "Inicia sesión para guardar títulos."
                
                return;
            }

            // Se cierran otros dropdown abiertos para tener uno solo activo
            statusWrappers.forEach(other => {
                if (other !== wrapper) {
                    other.classList.remove("open"
                }
            }

            wrapper.classList.toggle("open"

        }

        /* Click en opción de estado ---------------------------------------------------*/

        options.forEach(option => {

            option.addEventListener("click", async (e) => {

                e.stopPropagation(
                // Datos de la peli/serie obtenidos del wrapper html
                const mediaId = wrapper.dataset.mediaId;
                const mediaType = wrapper.dataset.mediaType;
                const status = option.dataset.status; // WATCHED | WATCHLIST
                const title = wrapper.dataset.title;
                const posterPath = wrapper.dataset.posterPath;
                const voteAverage = wrapper.dataset.voteAverage;

                try {
                    // POST al backend con snapshot data (para mostrar en perfil sin hacer repetir llamadas a la API)
                    const response = await fetch("/status", {

                            method: "POST",

                            headers: {
                                "Content-Type": "application/json",
                                ...(csrfToken && { "X-CSRF-TOKEN": csrfToken })
                            },

                            body: JSON.stringify({
                                mediaId,
                                mediaType,
                                title,
                                posterPath,
                                voteAverage,
                                status
                            })

                        }

                    if (!response.ok)
                    {
                        throw new Error("Error guardando estado"
                    }
                    const data = await response.json(
                    // Actualizar boton según el nuevo estado
                    updateStatusButton(button, data.status
                    // Cerrrar dropdown
                    wrapper.classList.remove("open"

                } catch (error) {
                    console.error("Error guardando estado del título:", error

                }

            }

        }

    }

    /* Cerrar dropdown si click fuera ------------------------------------------------ */
    document.addEventListener("click", () => {

        document.querySelectorAll(".media-status-wrapper")
            .forEach(wrapper => {
                wrapper.classList.remove("open"
            }

    }

}


/**
 * Actualiza el botón principal según el estado guardado.
 * Cambia icono, texto y clases CSS.
 *
 * @param button - El botón .btn-status a actualizar
 * @param status - El nuevo estado (WATCHED, WATCHLIST, o null)
 */
function updateStatusButton(button, status) {

    const icon = button.querySelector("i"
    const text = button.querySelector("span"

    // Limpiar clases previas
    button.classList.remove("watched","watchlist" 

    /* Sin estado > Mostrar Guardar en el icono en lugar del estado*/
    if (!status) {
        icon.className =
            "bi bi-bookmark-plus";
        text.textContent =
            "Guardar";
        return;

    }

    /* Estado WATCHED: "Vista" con icono de ojo */
    if (status === "WATCHED") {
        icon.className =
            "bi bi-eye-fill";
        text.textContent =
            "Vista";
        button.classList.add("watched"
    }

    /* Estado WATCHLIST: "Pendiente" con icono de reloj */
    if (status === "WATCHLIST") {
        icon.className =
            "bi bi-clock-fill";
        text.textContent =
            "Pendiente";
        button.classList.add("watchlist"
    }

}

/**
 * Crea un mensaje flash temporal (error/éxito) en la UI.
 *
 * @param type - Tipo del mensaje (error, success, etc.)
 * @param message - Texto del mensaje
 */
function showFlashMessage(type, message) {

    const flashContainer =
        document.querySelector(".flash-container"

    if (!flashContainer) return;

    //Eliminar flash anterior si existe
    const existingFlash = flashContainer.querySelector(".flash-alert"
    if (existingFlash) {
        existingFlash.remove(
    }

    // Crear nuevo flash
    const flash = document.createElement("div"
    flash.className = `flash-alert ${type}`;
    flash.innerHTML = `
        <span>${message}</span>
        <button class="flash-close">&times;</button>
    `;

    flashContainer.appendChild(flash

    // Cierre manual al hacer clic en la "x"
    flash.querySelector(".flash-close")
        .addEventListener("click", () => {
            flash.remove(
        }

    // Autocierre transcurridos los 4 segundos
    setTimeout(() => {
        flash.remove(
    }, 4000

}