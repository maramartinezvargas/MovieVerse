document.addEventListener("DOMContentLoaded", () => {

    /*NAV / BURGER -------------------------------------------------------------------------- */

    const burger = document.getElementById("burger-btn"
    const nav = document.getElementById("main-nav"

    if (burger && nav) {
        burger.addEventListener("click", () => {
            burger.classList.toggle("open"
            nav.classList.toggle("open"
        }

        document.querySelectorAll(".main-nav a").forEach(link => {
            link.addEventListener("click", () => {
                burger.classList.remove("open"
                nav.classList.remove("open"
            }
        }
    }

    /* SEARCH -------------------------------------------------------------------------- */

    const input = document.getElementById("search-input"
    const resultsBox = document.getElementById("search-results"

    if (input && resultsBox) {

        let timeout = null;

        input.addEventListener("input", () => {

            clearTimeout(timeout

            timeout = setTimeout(async () => {

                const query = input.value.trim(

                if (query.length < 2) {
                    resultsBox.innerHTML = "";
                    resultsBox.classList.add("hidden"
                    return;
                }

                try {
                    const res = await fetch(`/api/search?query=${encodeURIComponent(query)}`
                    const data = await res.json(
                    renderResults(data
                } catch (err) {
                    console.error("Error en búsqueda:", err
                }

            }, 300
        }

        function renderResults(data) {

            resultsBox.innerHTML = "";

            if (!data || data.length === 0) {
                resultsBox.classList.add("hidden"
                return;
            }

            resultsBox.classList.remove("hidden"

            data.forEach(item => {

                const url = item.mediaType === "movie"
                    ? `/peliculas/${item.id}`
                    : `/series/${item.id}`;

                let year = "";

                if (item.releaseDate?.length >= 4) {
                    year = item.releaseDate.substring(0, 4
                }

                if (!year && item.firstAirDate?.length >= 4) {
                    year = item.firstAirDate.substring(0, 4
                }

                const div = document.createElement("div"
                div.classList.add("search-item"

                div.innerHTML = `
                    <img src="${item.posterPath}" alt="${item.title}">
                    <div class="search-info">
                        <div class="search-title-row">
                            <span class="search-title">${item.title}</span>
                            ${year ? `<span class="search-year">${year}</span>` : ""}
                        </div>
                        <div class="search-type">
                            ${item.mediaType === "movie" ? "Película" : "Serie"}
                        </div>
                    </div>
                `;

                div.onclick = () => window.location.href = url;

                resultsBox.appendChild(div
            }
        }

        document.addEventListener("click", (e) => {
            const searchContainer = document.querySelector(".search-container"

            if (searchContainer && !searchContainer.contains(e.target)) {
                resultsBox.classList.add("hidden"
            }
        }
    }

    /* Login -------------------------------------------------------------------------- */

    const password = document.getElementById("password"
    const confirmPassword = document.getElementById("confirmPassword"

    if (password && confirmPassword) {

        function validatePassword() {
            if (password.value !== confirmPassword.value) {
                confirmPassword.setCustomValidity("Las contraseñas no coinciden"
            } else {
                confirmPassword.setCustomValidity(""
            }
        }

        password.addEventListener("input", validatePassword

        confirmPassword.addEventListener("input", () => {
            validatePassword(
            confirmPassword.reportValidity(
        }

        confirmPassword.addEventListener("blur", validatePassword
    }

    /* TRAILER MODAL -------------------------------------------------------------------------- */

    const modalEl = document.getElementById("trailerModal"
    const iframe = document.getElementById("trailerIframe"

    if (modalEl && iframe) {

        document.querySelectorAll("[data-trailer]").forEach(button => {

            button.addEventListener("click", () => {

                const key = button.dataset.trailer;
                if (!key) return;

                iframe.src = `https://www.youtube.com/embed/${key}?autoplay=1`;

                const modal = new bootstrap.Modal(modalEl
                modal.show(
            }
        }

        modalEl.addEventListener("hidden.bs.modal", () => {
            iframe.src = "";
        }
    }

    /* REVIEWS: TOGGLE LIST -------------------------------------------------------------------------- */
    const btn = document.getElementById("toggle-reviews-btn"

    if (btn) {

        let expanded = false;

        btn.addEventListener("click", () => {
            const hiddenReviews = document.querySelectorAll(".hidden-review"

            if (!expanded) {
                hiddenReviews.forEach(el => el.style.display = "block"
                expanded = true;
            } else {
                hiddenReviews.forEach(el => el.style.display = "none"

                document.querySelector(".reviews-section")
                    ?.scrollIntoView({ behavior: "smooth" }

                expanded = false;
            }

            // ICONO + TEXTO
            btn.innerHTML = expanded
                ? '<i class="bi bi-chevron-up"></i> Ver menos reseñas'
                : '<i class="bi bi-chevron-down"></i> Ver más reseñas';

            updateReadMoreButtons(
        }
    }

    /* STAR RATING -------------------------------------------------------------------------- */
    document.querySelectorAll(".star-rating").forEach(rating => {
        const stars = rating.querySelectorAll(".star"
        const ratingInput = rating.querySelector(".rating-input"
        const ratingText = rating.querySelector(".rating-value"

        if (!stars.length || !ratingInput) return;

        let selectedValue = 0;

        stars.forEach(star => {

            const value = parseInt(star.dataset.value

            star.addEventListener("mouseover", () => {
                highlightStars(value

                if (ratingText) {
                    ratingText.textContent = `${value}/10`;
                }
            }

            star.addEventListener("click", () => {
                selectedValue = value;
                ratingInput.value = value;
                setSelected(value
                rating.classList.remove("rating-error"

                if (ratingText) {
                    ratingText.textContent = `${value}/10`;
                }
            }

        }

        rating.addEventListener("mouseleave", () => {
            highlightStars(selectedValue

            if (ratingText) {
                ratingText.textContent = `${selectedValue}/10`;
            }
        }

        function highlightStars(value) {
            stars.forEach(star => {
                const starValue = parseInt(star.dataset.value
                star.classList.toggle("hovered", starValue <= value
            }
        }

        function setSelected(value) {
            stars.forEach(star => {
                const starValue = parseInt(star.dataset.value

                star.classList.toggle("selected", starValue <= value
                star.classList.toggle("bi-star-fill", starValue <= value
                star.classList.toggle("bi-star", starValue > value
            }
        }

    }
    updateReadMoreButtons(

}


/* REVIEW TEXT TOGGLE (leer más/menos) ------------------------------------------------------------------------ */
function toggleReview(button) {
    const text = button.closest(".review-card").querySelector(".review-text"
    const label = button.querySelector(".label"
    const icon = button.querySelector("i"

    text.classList.toggle("expanded"

    if (text.classList.contains("expanded")) {
        label.textContent = "Leer menos";
        icon.classList.replace("bi-chevron-down", "bi-chevron-up"
    } else {
        label.textContent = "Leer más";
        icon.classList.replace("bi-chevron-up", "bi-chevron-down"
    }
}

/* CONTROL BOTONES "LEER MÁS" */
function updateReadMoreButtons() {
    document.querySelectorAll(".review-card").forEach(card => {

        const text = card.querySelector(".review-text"
        const button = card.querySelector(".read-more-btn"

        if (!text || !button) return;

        const label = button.querySelector(".label"
        const icon = button.querySelector("i"

        // mantener estado visual correcto
        if (text.classList.contains("expanded")) {
            label.textContent = "Leer menos";
            icon.classList.replace("bi-chevron-down", "bi-chevron-up"
            button.style.display = "inline-flex";
            return; // ← IMPORTANTE: no recalcular nada más
        }

        // detectar si el texto está truncado (line-clamp hack)
        const isClamped = text.scrollHeight > text.clientHeight + 1;

        if (!isClamped) {
            button.style.display = "none";
        } else {
            button.style.display = "inline-flex";
            label.textContent = "Leer más";
            icon.classList.replace("bi-chevron-up", "bi-chevron-down"
        }
    }
}


