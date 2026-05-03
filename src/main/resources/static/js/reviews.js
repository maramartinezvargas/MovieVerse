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

}