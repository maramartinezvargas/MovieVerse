/**
 * reviews.js
 *
 * Gestiona la UI de reseñas en la aplicación:
 * - Expandir/contraer textos largos ("Leer más/menos")
 * - Auto-cerrar flash alerts (mensajes temporales de feedback)
 * - Validación de rating en formularios de reseñas
 * - Toggle "Ver más reseñas" en perfil
 * - Inicializar estrellas en modales de edición según rating actual
 */

/**
 * Alterna la expansión de un texto de reseña.
 * Actualiza el botón ("Leer más/menos") y el icono (rotación).
 *
 * @param {Event} event - Evento del clic
 * @param {HTMLElement} button - Botón que dispara la acción
 */
function toggleReview(event, button) {

    event.preventDefault(
    event.stopPropagation(

    // Encontrar el elemento de texto según la clase del contenedor
    const reviewText = button.closest(".profile-review-card, .review-card")
                            .querySelector(".profile-review-comment, .review-text"

    reviewText.classList.toggle("expanded"

    const expanded = reviewText.classList.contains("expanded"

    //Actualizar texto del botón
    button.querySelector(".label").textContent = expanded ? "Leer menos" : "Leer más";

    // Rotár icono si existe
    const icon = button.querySelector("i"

    if (icon) {
        icon.classList.toggle("rotated"
    }
}

document.addEventListener('DOMContentLoaded', () => {

    /* Flash alerts ---------------------------------------------------------------------- */
    // Auto-cerrar mensajes temporales de feedback y permitir también el cierre manual (x)

    const flashAlerts = document.querySelectorAll('.flash-alert'
    if (flashAlerts.length) {
        document.querySelectorAll('.flash-close').forEach(btn => {
            btn.addEventListener('click', () => {
                btn.closest('.flash-alert').remove(
            }
        }
        // Autodimiss transcurridos 4 segundos
        setTimeout(() => {flashAlerts.forEach(el => el.remove()}, 4000
    }

    /* Validación formulario reseñas ------------------------------------------------------ */
    // Valida que haya una calificación (rating) antes de enviar el formulario de reseña.
    // Muestra error visual si falla.
    document.querySelectorAll(".review-modal form").forEach(form => {

        form.addEventListener("submit", (e) => {
            const ratingInput = form.querySelector(".rating-input"
            const ratingWrapper = form.querySelector(".star-rating"
            const errorText = form.querySelector(".rating-error-text"

            if (!ratingInput.value) {

                e.preventDefault(
                ratingWrapper.classList.add("rating-error"

                if (errorText) {errorText.classList.add("visible"}

                // Limpiar error visual al hacer clic en las estrellas (solo una vez)
                ratingWrapper.addEventListener("click", () => {
                    ratingWrapper.classList.remove("rating-error"
                    if (errorText) {errorText.classList.remove("visible"}
                }, { once: true }
            }
        }
    }

    /* Mostrar "Leer más" solo si hay overflow */

    /*document.querySelectorAll(".profile-review-comment").forEach(comment => {

        requestAnimationFrame(() => {
            const footer =
                comment.parentElement.querySelector(
                    ".profile-review-footer"
                

            if (!footer) return;

            const needsClamp =
                comment.scrollHeight > comment.clientHeight + 1;

            if (!needsClamp) {
                footer.style.display = "none";
            }
        }
    }*/

    /* Toggle "ver más reseñas" ----------------------------------*/
    // Alterna entre mostrar/ocultar reseñas adicionales en el perfil
    const toggleBtn =
        document.getElementById("toggle-reviews-btn"

    if (toggleBtn) {

        const hiddenReviews =
            document.querySelectorAll(".hidden-review"

        let expanded = false;

        toggleBtn.addEventListener("click", () => {

            expanded = !expanded;

            hiddenReviews.forEach(review => {
                review.classList.toggle("show-review"
            }

            // Actualizar texto e icono del botón
            toggleBtn.innerHTML = expanded
                ? '<i class="bi bi-chevron-up"></i> Ver menos reseñas'
                : '<i class="bi bi-chevron-down"></i> Ver más reseñas';

        }

    }

    /* Inicializar estrellas en modales de edición -------------------------------------------------*/
    // Cuando se abre un modal de edición se llenan las estrellas con el rating guardado (si existe)
    document.querySelectorAll(".review-modal").forEach(modal => {

        const ratingInput = modal.querySelector(".rating-input"
        const stars = modal.querySelectorAll(".star"
        const ratingValue = modal.querySelector(".rating-value"

        if (!ratingInput || !stars.length) return;

        const currentRating = parseInt(ratingInput.value

        // Si no hay rating o es inválido, se deja todas las reseñas vacías.-
        console.log("rating actual:", ratingInput.value
        if (isNaN(currentRating) || currentRating <= 0) return;

        // Se rellenan las estrellas hasta el rating actual
        stars.forEach(star => {
            const value = parseInt(star.dataset.value
            star.className = value <= currentRating ? "bi bi-star-fill star" : "bi bi-star star";
        }

        if (ratingValue) {
            ratingValue.textContent = `${currentRating}/10`;
        }
    }
}