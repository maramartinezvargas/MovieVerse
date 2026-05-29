/**
 * likes.js
 *
 * Gestiona el botón de "me gusta" en la ficha de un medio:
 * - Envía una petición POST a /likes/toggle para alternar el like
 * - Actualiza el icono y el contador en la UI
 * - Muestra mensajes de error/estado usando flash messages
 *
 */
document.addEventListener("DOMContentLoaded", () => {

    const likeBtn = document.getElementById("likeBtn"

    if (!likeBtn) return;

    const mediaId = likeBtn.dataset.mediaId;
    const mediaType = likeBtn.dataset.mediaType;
    const isAuthenticated = likeBtn.dataset.authenticated === "true";

    const likeCount = document.getElementById("likeCount"
    const icon = likeBtn.querySelector("i"

    // CSRF opcional
    const csrfInput = document.querySelector("input[name='_csrf']"
    const csrfToken = csrfInput ? csrfInput.value : null;

    likeBtn.addEventListener("click", async () => {

        // usuario no autenticado
        if (!isAuthenticated) {

            showFlashMessage(
                "error",
                "Inicia sesión para dar me gusta."
            

            return;
        }

        try {

            const response = await fetch(`/likes/toggle`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    ...(csrfToken && { "X-CSRF-TOKEN": csrfToken })
                },
                body: new URLSearchParams({
                    mediaId: mediaId,
                    mediaType: mediaType
                })
            }

            const data = await response.json(

            // cambiar icono
            if (data.liked) {

                icon.classList.remove("bi-heart"
                icon.classList.add("bi-heart-fill"

                likeBtn.classList.add("liked"

            } else {

                icon.classList.remove("bi-heart-fill"
                icon.classList.add("bi-heart"

                likeBtn.classList.remove("liked"
            }

            // actualizar contador
            likeCount.textContent = data.totalLikes;

        } catch (error) {

            console.error("Error en like:", error

            showFlashMessage(
                "error",
                "Ha ocurrido un error al procesar el like."
            
        }

    }

    function showFlashMessage(type, message) {

        const flashContainer = document.querySelector(".flash-container"

        if (!flashContainer) return;

        const existingFlash = flashContainer.querySelector(".flash-alert"

        if (existingFlash) {
            existingFlash.remove(
        }

        const flash = document.createElement("div"

        flash.className = `flash-alert ${type}`;

        flash.innerHTML = `
            <span>${message}</span>
            <button class="flash-close">&times;</button>
        `;

        flashContainer.appendChild(flash

        flash.querySelector(".flash-close").addEventListener("click", () => {
            flash.remove(
        }

        setTimeout(() => {
            flash.remove(
        }, 4000
    }

}