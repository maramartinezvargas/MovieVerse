function toggleReview(event, button) {

    event.preventDefault(
    event.stopPropagation(

    const reviewText =
        button.closest(".profile-review-card, .review-card")
            .querySelector(".profile-review-comment, .review-text"

    reviewText.classList.toggle("expanded"

    const expanded =
        reviewText.classList.contains("expanded"

    button.querySelector(".label").textContent =
        expanded ? "Leer menos" : "Leer más";

    const icon = button.querySelector("i"

    if (icon) {
        icon.classList.toggle("rotated"
    }
}

document.addEventListener('DOMContentLoaded', () => {

    /* Flash alerts */

    const flashAlerts = document.querySelectorAll('.flash-alert'

    if (flashAlerts.length) {

        document.querySelectorAll('.flash-close').forEach(btn => {

            btn.addEventListener('click', () => {
                btn.closest('.flash-alert').remove(
            }

        }

        setTimeout(() => {
            flashAlerts.forEach(el => el.remove()
        }, 4000
    }

    /* Validación formulario reseñas */

    document.querySelectorAll(".review-modal form").forEach(form => {

        form.addEventListener("submit", (e) => {

            const ratingInput =
                form.querySelector(".rating-input"

            const ratingWrapper =
                form.querySelector(".star-rating"

            const errorText =
                form.querySelector(".rating-error-text"

            if (!ratingInput.value) {

                e.preventDefault(

                ratingWrapper.classList.add("rating-error"

                if (errorText) {
                    errorText.classList.add("visible"
                }

                ratingWrapper.addEventListener("click", () => {

                    ratingWrapper.classList.remove("rating-error"

                    if (errorText) {
                        errorText.classList.remove("visible"
                    }

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

    /* Toggle ver más reseñas */

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

            toggleBtn.innerHTML = expanded
                ? '<i class="bi bi-chevron-up"></i> Ver menos reseñas'
                : '<i class="bi bi-chevron-down"></i> Ver más reseñas';

        }

    }

}