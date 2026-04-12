document.addEventListener("DOMContentLoaded", () => {

    /* =========================
       NAV / BURGER
    ========================= */

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

    /* =========================
       SEARCH
    ========================= */

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

    /* =========================
       TRAILER MODAL
    ========================= */

    const modalEl = document.getElementById("trailerModal"
    const iframe = document.getElementById("trailerIframe"

    if (modalEl && iframe) {

        document.querySelectorAll("[data-trailer]").forEach(button => {

            button.addEventListener("click", () => {

                const key = button.dataset.trailer;

                if (!key) {
                    console.warn("No hay trailer disponible"
                    return;
                }

                iframe.src = `https://www.youtube.com/embed/${key}?autoplay=1`;

                const modal = new bootstrap.Modal(modalEl
                modal.show(
            }
        }

        // limpiar vídeo al cerrar
        modalEl.addEventListener("hidden.bs.modal", () => {
            iframe.src = "";
        }
    }

}