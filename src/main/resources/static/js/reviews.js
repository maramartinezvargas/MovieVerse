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

    /* Validación del formulario de reseñas */

    document.querySelectorAll(".review-modal form").forEach(form => {

        form.addEventListener("submit", (e) => {

            const ratingInput = form.querySelector(".rating-input"
            const ratingWrapper = form.querySelector(".star-rating"
            const errorText = form.querySelector(".rating-error-text"

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

    /*Reviews en profile.html  ----------------------------------------------------------------------- */
    function toggleReview(button) {

        const reviewText =
            button.closest(".review-card")
                .querySelector(".review-text"

        reviewText.classList.toggle("expanded"

        const expanded =
            reviewText.classList.contains("expanded"

        button.querySelector(".label").textContent =
            expanded ? "Leer menos" : "Leer más";
    }

    document.addEventListener("DOMContentLoaded", () => {

        const toggleBtn =
            document.getElementById("toggle-reviews-btn"

        if (!toggleBtn) return;

        const hiddenReviews =
            document.querySelectorAll(".hidden-review"

        let expanded = false;

        toggleBtn.addEventListener("click", () => {

            expanded = !expanded;

            hiddenReviews.forEach(review => {
                review.style.display =
                    expanded ? "block" : "none";
            }

            toggleBtn.innerHTML = expanded
                ? '<i class="bi bi-chevron-up"></i> Ver menos reseñas'
                : '<i class="bi bi-chevron-down"></i> Ver más reseñas';
        }

    }


}