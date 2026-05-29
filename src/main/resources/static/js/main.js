/**
 * main.js - Funcionalidades interactivas principales de la aplicación MovieVerse
 *
 * Gestiona los siguientes componentes de la UI:
 * - Navegación responsiva (menú hamburguesa personalizado)
 * - Búsqueda en tiempo real de películas/series (resultados dinámicos)
 * - Validación de contraseñas (registro/login)
 * - Modal de trailers de YouTube
 * - Toggle de listados de reseñas
 * - Sistema de valoración por estrellas
 * - Expansión/colapso de textos largos ("Leer más/menos")
 *
 * @author Tamara Martinez Vargas
 * @since 02/03/2026
 * @version 28/05/2026
 */

document.addEventListener("DOMContentLoaded", () => {

    /**
     * MENÚ HAMBURGUESA
     * Toggle del menú de navegación responsivo
     */
    const burger = document.getElementById("burger-btn"
    const nav = document.getElementById("main-nav"

    if (burger && nav) {
        burger.addEventListener("click", () => {
            burger.classList.toggle("open"
            nav.classList.toggle("open"
        }

        // Cerrar menú al hacer clic en un enlace
        document.querySelectorAll(".main-nav a").forEach(link => {
            link.addEventListener("click", () => {
                burger.classList.remove("open"
                nav.classList.remove("open"
            }
        }
    }


    /**
     * BÚSQUEDA EN TIEMPO REAL
     * Consulta la API /api/search mientras el usuario escribe
     * Implementa debounce de 300ms para evitar sobre-solicitudes
     */
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

            /**
             * Renderiza resultados de búsqueda en el DOM
             * @param {Array} data - Array de resultados (películas/series)
             */
            function renderResults(data) {

            resultsBox.innerHTML = "";

            if (!data || data.length === 0) {
                resultsBox.classList.add("hidden"
                return;
            }

            resultsBox.classList.remove("hidden"

            data.forEach(item => {
                // Construir URL según tipo de contenido
                const url = item.mediaType === "movie"
                    ? `/peliculas/${item.id}`
                    : `/series/${item.id}`;

                // Extraer año de la fecha
                let year = "";

                if (item.releaseDate?.length >= 4) {
                    year = item.releaseDate.substring(0, 4
                }

                if (!year && item.firstAirDate?.length >= 4) {
                    year = item.firstAirDate.substring(0, 4
                }

                // Crear el elemento visual del resultado
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

        // Cerrar resultados cuando el usuario hace clic fuera de la búsqueda
        document.addEventListener("click", (e) => {
            const searchContainer = document.querySelector(".search-container"

            if (searchContainer && !searchContainer.contains(e.target)) {
                resultsBox.classList.add("hidden"
            }
        }
    }


    /**
     * VALIDACIÓN DE CONTRASEÑAS (LOGIN/REGISTRO)
     * Valida que ambos campos coincidan en tiempo real
     */
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


    /**
     * MODAL DE TRAILERS
     * Carga trailers de YouTube en un modal automáticamente
     * Detiene la reproducción al cerrar el modal
     */
    const modalEl = document.getElementById("trailerModal"
    const iframe = document.getElementById("trailerIframe"

    if (modalEl && iframe) {

        document.querySelectorAll("[data-trailer]").forEach(button => {

            button.addEventListener("click", () => {

                const key = button.dataset.trailer;
                if (!key) return;

                // Iniciar reproducción automática
                iframe.src = `https://www.youtube.com/embed/${key}?autoplay=1`;

                const modal = new bootstrap.Modal(modalEl
                modal.show(
            }
        }

        // Detener reproducción al cerrar el modal
        modalEl.addEventListener("hidden.bs.modal", () => {
            iframe.src = "";
        }
    }


    /**
     * TOGGLE DE LISTADO DE RESEÑAS
     * Alterna entre mostrar/ocultar reseñas adicionales
     */
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

            // Actualizar texto e icono del botón
            btn.innerHTML = expanded
                ? '<i class="bi bi-chevron-up"></i> Ver menos reseñas'
                : '<i class="bi bi-chevron-down"></i> Ver más reseñas';

            updateReadMoreButtons(
        }
    }


    /**
     * SISTEMA DE VALORACIÓN POR ESTRELLAS
     * Permite seleccionar calificación de 1-10 interactivamente
     */
    document.querySelectorAll(".star-rating").forEach(rating => {
        const stars = rating.querySelectorAll(".star"
        const ratingInput = rating.querySelector(".rating-input"
        const ratingText = rating.querySelector(".rating-value"

        if (!stars.length || !ratingInput) return;

        let selectedValue = 0;

        stars.forEach(star => {

            const value = parseInt(star.dataset.value

            // Mostrar vista previa al pasar el mouse
            star.addEventListener("mouseover", () => {
                highlightStars(value

                if (ratingText) {
                    ratingText.textContent = `${value}/10`;
                }
            }

            //Registrar selección al hacer clic
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

        // Restaurar visualización anterior al salir
        rating.addEventListener("mouseleave", () => {
            highlightStars(selectedValue

            if (ratingText) {
                ratingText.textContent = `${selectedValue}/10`;
            }
        }


        /**
         * Resalta estrellas hasta el valor especificado
         * @param {number} value - Número de estrellas a resaltar (1-10)
         */
        function highlightStars(value) {
            stars.forEach(star => {
                const starValue = parseInt(star.dataset.value
                star.classList.toggle("hovered", starValue <= value
            }
        }

        /**
         * Marca estrellas como seleccionadas visualmente
         * @param {number} value - Calificación final (1-10)
         */
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


/**
 * Toggle "Leer más/menos" en textos de reseñas
 * Expande/contrae comentarios largos (>180 caracteres)
 * @param {Event} event - Evento del clic
 * @param {HTMLElement} button - Botón que dispara la acción
 */
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


/**
 * Actualiza visibilidad de botones "Leer más" basado en truncamiento de texto
 * Detecta si el contenido está truncado por CSS line-clamp
 */
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
            return; // IMPORTANTE: si el texto ya está expandido, se mantiene estado visual ("Leer menos")
                    // y se sale aquí para evitar la comprobación posterior que podría ocultar el botón por error.

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


