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
    const grid = document.getElementById('series-grid'

    const yearSelect = document.getElementById('filter-year'
    const genreSelect = document.getElementById('filter-genre'
    const sortSelect = document.getElementById('filter-sort'

    const ratingInput = document.getElementById('filter-rating'
    const votesInput = document.getElementById('filter-votes'

    const ratingValue = document.getElementById('rating-value'
    const votesValue = document.getElementById('votes-value'

    const toggleFiltersBtn = document.getElementById('toggle-filters'
    const filtersContainer = document.getElementById('filters-container'

    if (!button || !grid) return;

    const loadedIds = new Set(

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
        if (id) loadedIds.add(id
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

    /* ================= FILTROS ================= */

    if (yearSelect) {
        yearSelect.addEventListener('change', () => {
            currentYear = yearSelect.value;
            resetAndLoad(
        }
    }

    if (genreSelect) {
        genreSelect.addEventListener('change', () => {
            currentGenre = genreSelect.value;
            resetAndLoad(
        }
    }

    if (sortSelect) {
        sortSelect.addEventListener('change', () => {
            currentSort = sortSelect.value;
            resetAndLoad(
        }
    }

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
        let url = `/api/series?page=${currentPage}`;

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
        loadedIds.clear(

        closeFilters(
        loadSeries(
    }

    /* ================= LOAD ================= */

    async function loadSeries() {

        button.disabled = true;
        button.textContent = 'Cargando...';

        try {
            const response = await fetch(buildUrl()

            if (!response.ok) throw new Error('Error al cargar series'

            const series = await response.json(

            if (!series || series.length === 0) {
                button.textContent = 'No hay más series';
                return;
            }

            series.forEach((tv, index) => {

                if (loadedIds.has(String(tv.id))) return;
                loadedIds.add(String(tv.id)

                if (!tv.posterPath) return;

                const div = document.createElement('div'
                div.classList.add('movie-card'

                div.innerHTML = `
                    <a href="/series/${tv.id}">
                        <img src="${tv.posterPath}" alt="poster">
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
        await loadSeries(
    }

    /* ================= RESET BUTTON ================= */

    if (resetButton) {
        resetButton.addEventListener('click', () => {

            currentPage = 1;
            currentYear = '';
            currentGenre = '';
            currentSort = DEFAULTS.sort;
            minRating = DEFAULTS.rating;
            minVotes = DEFAULTS.votes;

            if (yearSelect) yearSelect.value = '';
            if (genreSelect) genreSelect.value = '';
            if (sortSelect) sortSelect.value = DEFAULTS.sort;

            if (ratingInput) {
                ratingInput.value = DEFAULTS.rating;
                if (ratingValue) ratingValue.textContent = DEFAULTS.rating;
            }

            if (votesInput) {
                votesInput.value = DEFAULTS.votes;
                if (votesValue) votesValue.textContent = DEFAULTS.votes;
            }

            grid.innerHTML = '';
            loadedIds.clear(

            closeFilters(
            loadSeries(
        }
    }

}