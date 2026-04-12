document.addEventListener("DOMContentLoaded", () => {

    const burger = document.getElementById("burger-btn"
    const nav = document.getElementById("main-nav"

    burger.addEventListener("click", () => {
        burger.classList.toggle("open"
        nav.classList.toggle("open"
    }

    document.querySelectorAll(".main-nav a").forEach(link=>{
        link.addEventListener("click",()=>{
            burger.classList.remove("open"
            nav.classList.remove("open"
        }
    }

    // Buscador
    const input = document.getElementById("search-input"
    const resultsBox = document.getElementById("search-results"

    if (!input || !resultsBox) return;

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

            // Extraer año
            let year = "";

            if (item.releaseDate && item.releaseDate.length >= 4) {
                year = item.releaseDate.substring(0, 4
            }

            // fallback por si viene otro campo o vacío
            if (!year && item.firstAirDate && item.firstAirDate.length >= 4) {
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

            div.dataset.type = item.mediaType;

            div.onclick = () => window.location.href = url;

            resultsBox.appendChild(div
            console.log(data
            console.log(item
        }

    }

    document.addEventListener("click", (e) => {

        const searchContainer = document.querySelector(".search-container"

        if (!searchContainer.contains(e.target)) {
            resultsBox.classList.add("hidden"
        }
    }
}