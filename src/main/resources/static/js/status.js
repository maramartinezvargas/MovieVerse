document.addEventListener("DOMContentLoaded", () => {
    const csrfInput = document.querySelector("input[name='_csrf']"

    const csrfToken = csrfInput ? csrfInput.value : null;

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

        /* Abrir / cerrar dropdown */

        button.addEventListener("click", (e) => {

            e.stopPropagation(

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