document.addEventListener('DOMContentLoaded', () => {

    const DEFAULTS = {
        sort: 'popularity.desc',
        rating: 0,
        votes: 0
    };

    const resetButton = document.getElementById('reset-filters'

    let currentPage = 1;
    let currentYear = '';
    let currentGenre = '';
    let currentSort = DEFAULTS.sort;
    let minRating = DEFAULTS.rating;
    let minVotes = DEFAULTS.votes;

    const button = document.getElementById('load-more-btn'
    const grid = document.getElementById('movies-grid'

    const ratingInput = document.getElementById('filter-rating'
    const votesInput = document.getElementById('filter-votes'

    const ratingValue = document.getElementById('rating-value'
    const votesValue = document.getElementById('votes-value'

    const toggleFiltersBtn = document.getElementById('toggle-filters'
    const filtersContainer = document.getElementById('filters-container'

    if (!button || !grid) return;

    const loadedMovieIds = new Set(

    /* ================= DROPDOWN GENÉRICO ================= */

    function initDropdown({ target, onChange }) {

        const selected = document.getElementById(`${target}-selected`
        const options = document.getElementById(`${target}-options`

        if (!selected || !options) return;

        selected.addEventListener("click", () => {
            options.classList.toggle("hidden"
        }

        options.querySelectorAll(".dropdown-option").forEach(option => {
            option.addEventListener("click", () => {

                const icon = selected.querySelector("i"
                selected.innerHTML = `${option.textContent} `;
                if (icon) selected.appendChild(icon

                options.querySelectorAll(".dropdown-option")
                    .forEach(o => o.classList.remove("active")

                option.classList.add("active"

                options.classList.add("hidden"

                onChange(option.dataset.value
            }
        }

        document.addEventListener("click", (e) => {
            if (!e.target.closest(`[data-target="${target}"]`)) {
                options.classList.add("hidden"
            }
        }
    }

    /* ================= INIT DROPDOWNS ================= */

    initDropdown({
        target: "genre",
        onChange: (value) => {
            currentGenre = value;
            resetAndLoad(
        }
    }

    initDropdown({
        target: "year",
        onChange: (value) => {
            currentYear = value;
            resetAndLoad(
        }
    }

    initDropdown({
        target: "sort",
        onChange: (value) => {
            currentSort = value || DEFAULTS.sort;
            resetAndLoad(
        }
    }

    /* ================= TOGGLE FILTROS ================= */

    function toggleFilters() {
        filtersContainer.classList.toggle('open'

        if (toggleFiltersBtn) {
            toggleFiltersBtn.innerHTML =
                filtersContainer.classList.contains('open')
                    ? '<i class="bi bi-x-lg"></i> Cerrar filtros'
                    : '<i class="bi bi-funnel-fill"></i> Filtrar';
        }
    }

    if (toggleFiltersBtn && filtersContainer) {
        toggleFiltersBtn.addEventListener('click', toggleFilters
    }

    function closeFilters() {
        if (filtersContainer) {
            filtersContainer.classList.remove('open'
        }

        if (toggleFiltersBtn) {
            toggleFiltersBtn.innerHTML = '<i class="bi bi-funnel-fill"></i> Filtrar';
        }
    }

    /* ================= INICIALIZACIÓN ================= */

    document.querySelectorAll('.movie-card a').forEach(a => {
        const id = a.getAttribute('href')?.split('/').pop(
        if (id) loadedMovieIds.add(id
    }

    document.querySelectorAll('.movie-card').forEach(el => {
        el.classList.add('show'
    }

    if (ratingInput) {
        ratingInput.value = DEFAULTS.rating;
        if (ratingValue) ratingValue.textContent = DEFAULTS.rating;
    }

    if (votesInput) {
        votesInput.value = DEFAULTS.votes;
        if (votesValue) votesValue.textContent = DEFAULTS.votes;
    }

    /* ================= SLIDERS ================= */

    if (ratingInput) {
        ratingInput.addEventListener('input', () => {
            minRating = parseFloat(ratingInput.value
            if (ratingValue) ratingValue.textContent = minRating;
        }

        ratingInput.addEventListener('change', resetAndLoad
    }

    if (votesInput) {
        votesInput.addEventListener('input', () => {
            minVotes = parseInt(votesInput.value
            if (votesValue) votesValue.textContent = minVotes;
        }

        votesInput.addEventListener('change', resetAndLoad
    }

    /* ================= URL ================= */

    function buildUrl() {
        let url = `/api/peliculas?page=${currentPage}`;

        if (currentYear) url += `&year=${currentYear}`;
        if (currentGenre) url += `&genre=${currentGenre}`;
        if (currentSort) url += `&sort=${currentSort}`;
        if (minRating > 0) url += `&minRating=${minRating}`;
        if (minVotes > 0) url += `&minVotes=${minVotes}`;

        return url;
    }

    /* ================= RESET ================= */

    function resetAndLoad() {
        currentPage = 1;
        grid.innerHTML = '';
        loadedMovieIds.clear(

        closeFilters(
        loadMovies(
    }

    if (resetButton) {
        resetButton.addEventListener('click', () => {

            currentPage = 1;
            currentYear = '';
            currentGenre = '';
            currentSort = DEFAULTS.sort;
            minRating = DEFAULTS.rating;
            minVotes = DEFAULTS.votes;

            grid.innerHTML = '';
            loadedMovieIds.clear(

            closeFilters(
            loadMovies(
        }
    }

    /* ================= LOAD ================= */

    async function loadMovies() {

        button.disabled = true;
        button.textContent = 'Cargando...';

        try {
            const response = await fetch(buildUrl()

            if (!response.ok) throw new Error('Error al cargar películas'

            const movies = await response.json(

            if (!movies || movies.length === 0) {
                button.textContent = 'No hay más películas';
                return;
            }

            movies.forEach((movie, index) => {

                if (loadedMovieIds.has(String(movie.id))) return;
                loadedMovieIds.add(String(movie.id)

                if (!movie.posterPath) return;

                const div = document.createElement('div'
                div.classList.add('movie-card'

                div.innerHTML = `
                    <a href="/peliculas/${movie.id}">
                        <img src="${movie.posterPath}" alt="poster">
                    </a>
                `;

                grid.appendChild(div

                setTimeout(() => {
                    div.classList.add('show'
                }, index * 60
            }

            button.disabled = false;
            button.textContent = 'Mostrar más';

        } catch (error) {
            console.error(error
            button.disabled = false;
            button.textContent = 'Error, reintentar';
        }
    }

    /* ================= PAGINACIÓN ================= */

    button.addEventListener('click', async () => {
        currentPage++;
        await loadMovies(
    }

}