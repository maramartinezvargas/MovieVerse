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

        /* Abrir / cerrar dropdown - Solo para usuarios autenticados */
        button.addEventListener("click", (e) => {

            e.stopPropagation(

            /* Usuario no autenticado */
            if (!isAuthenticated) {
                showFlashMessage(
                    "error",
                    "Inicia sesión para guardar títulos."
                
                return;
            }

            statusWrappers.forEach(other => {
                if (other !== wrapper) {
                    other.classList.remove("open"
                }
            }

            wrapper.classList.toggle("open"

        }

        /* Click opciones */

        options.forEach(option => {

            option.addEventListener("click", async (e) => {

                e.stopPropagation(

                const mediaId =
                    wrapper.dataset.mediaId;

                const mediaType =
                    wrapper.dataset.mediaType;

                const status =
                    option.dataset.status;

                try {

                    const response =
                        await fetch("/status", {

                            method: "POST",

                            headers: {
                                "Content-Type": "application/json",
                                ...(csrfToken && { "X-CSRF-TOKEN": csrfToken })
                            },

                            body: JSON.stringify({
                                mediaId,
                                mediaType,
                                status
                            })

                        }

                    if (!response.ok)
                    {
                        throw new Error("Error guardando estado"
                    }
                    const data = await response.json(
                    updateStatusButton(button, data.status
                    wrapper.classList.remove("open"

                } catch (error) {
                    console.error("Error guardando estado del título:", error

                }

            }

        }

    }

    /* Click fuera */
    document.addEventListener("click", () => {

        document.querySelectorAll(".media-status-wrapper")
            .forEach(wrapper => {
                wrapper.classList.remove("open"
            }

    }

}

/* Actualizar botón principal */

function updateStatusButton(button, status) {

    const icon = button.querySelector("i"

    const text = button.querySelector("span"

    button.classList.remove("watched","watchlist" 

    /* Sin estado */
    if (!status) {
        icon.className =
            "bi bi-bookmark-plus";
        text.textContent =
            "Guardar";
        return;

    }

    /* Vista */
    if (status === "WATCHED") {
        icon.className =
            "bi bi-eye-fill";
        text.textContent =
            "Vista";
        button.classList.add("watched"
    }

    /* Pendiente */
    if (status === "WATCHLIST") {
        icon.className =
            "bi bi-clock-fill";
        text.textContent =
            "Pendiente";
        button.classList.add("watchlist"
    }

}

function showFlashMessage(type, message) {

    const flashContainer =
        document.querySelector(".flash-container"

    if (!flashContainer) return;

    const existingFlash =
        flashContainer.querySelector(".flash-alert"

    if (existingFlash) {
        existingFlash.remove(
    }

    const flash =
        document.createElement("div"

    flash.className =
        `flash-alert ${type}`;

    flash.innerHTML = `
        <span>${message}</span>
        <button class="flash-close">&times;</button>
    `;

    flashContainer.appendChild(flash

    flash.querySelector(".flash-close")
        .addEventListener("click", () => {
            flash.remove(
        }

    setTimeout(() => {
        flash.remove(
    }, 4000

}