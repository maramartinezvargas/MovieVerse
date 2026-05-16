document.addEventListener('DOMContentLoaded', () => {

    window.initExplorer = function ({
                                        apiUrl,
                                        gridId,
                                        detailBasePath
                                    }) {

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
        const grid = document.getElementById(gridId

        const ratingInput = document.getElementById('filter-rating'
        const votesInput = document.getElementById('filter-votes'

        const ratingValue = document.getElementById('rating-value'
        const votesValue = document.getElementById('votes-value'

        const toggleFiltersBtn = document.getElementById('toggle-filters'
        const filtersContainer = document.getElementById('filters-container'

        if (!button || !grid) return;

        const loadedIds = new Set(

        /* ================= DROPDOWN ================= */

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

        initDropdown({ target: "genre", onChange: v => { currentGenre = v; resetAndLoad( }}
        initDropdown({ target: "year", onChange: v => { currentYear = v; resetAndLoad( }}
        initDropdown({ target: "sort", onChange: v => { currentSort = v || DEFAULTS.sort; resetAndLoad( }}

        /* ================= UI ================= */

        function applyStateToUI() {

            if (ratingInput) {
                ratingInput.value = minRating;
                ratingValue.textContent = minRating;
            }

            if (votesInput) {
                votesInput.value = minVotes;
                votesValue.textContent = minVotes;
            }

            document.getElementById('year-selected').innerHTML = 'Todos <i class="bi bi-chevron-down"></i>';
            document.getElementById('genre-selected').innerHTML = 'Todos <i class="bi bi-chevron-down"></i>';
            document.getElementById('sort-selected').innerHTML = 'Popularidad <i class="bi bi-chevron-down"></i>';

            document.querySelectorAll('.dropdown-option')
                .forEach(o => o.classList.remove('active')

            const defaultSort = document.querySelector('[data-value="popularity.desc"]'
            if (defaultSort) defaultSort.classList.add('active'
        }

        /* ================= TOGGLE ================= */

        function toggleFilters() {
            filtersContainer.classList.toggle('open'

            toggleFiltersBtn.innerHTML =
                filtersContainer.classList.contains('open')
                    ? '<i class="bi bi-x-lg"></i> Cerrar filtros'
                    : '<i class="bi bi-funnel-fill"></i> Filtrar';
        }

        toggleFiltersBtn?.addEventListener('click', toggleFilters

        function closeFilters() {
            filtersContainer.classList.remove('open'
            toggleFiltersBtn.innerHTML = '<i class="bi bi-funnel-fill"></i> Filtrar';
        }

        /* ================= INIT ================= */

        document.querySelectorAll('.movie-card a').forEach(a => {
            const id = a.getAttribute('href')?.split('/').pop(
            if (id) loadedIds.add(id
        }

        document.querySelectorAll('.movie-card').forEach(el => el.classList.add('show')

        applyStateToUI(

        /* ================= SLIDERS ================= */

        ratingInput?.addEventListener('input', () => {
            minRating = parseFloat(ratingInput.value
            ratingValue.textContent = minRating;
        }

        ratingInput?.addEventListener('change', resetAndLoad

        votesInput?.addEventListener('input', () => {
            minVotes = parseInt(votesInput.value
            votesValue.textContent = minVotes;
        }

        votesInput?.addEventListener('change', resetAndLoad

        /* ================= URL ================= */

        function buildUrl() {
            let url = `${apiUrl}?page=${currentPage}`;

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
            loadItems(
        }

        resetButton?.addEventListener('click', () => {

            currentPage = 1;
            currentYear = '';
            currentGenre = '';
            currentSort = DEFAULTS.sort;
            minRating = DEFAULTS.rating;
            minVotes = DEFAULTS.votes;

            applyStateToUI(

            grid.innerHTML = '';
            loadedIds.clear(

            closeFilters(
            loadItems(
        }

        /* ================= LOAD ================= */

        async function loadItems() {

            button.disabled = true;
            button.textContent = 'Cargando...';

            try {
                const response = await fetch(buildUrl()

                if (!response.ok) throw new Error('Error al cargar'

                const items = await response.json(

                if (!items.length) {
                    button.textContent = 'No hay más';
                    return;
                }

                items.forEach((item, index) => {
                    if (loadedIds.has(String(item.id))) return;
                    loadedIds.add(String(item.id)

                    if (!item.posterPath) return;

                    const div = document.createElement('div'

                    div.classList.add('movie-card'
                    div.innerHTML = `
                        <a href="${detailBasePath}/${item.id}">
                            <img src="${item.posterPath}" alt="poster">
                        </a>
                    `;

                    grid.appendChild(div

                    setTimeout(() => div.classList.add('show'), index * 60
                }

                button.disabled = false;
                button.textContent = 'Mostrar más';

            } catch (error) {
                console.error(error
                button.disabled = false;
                button.textContent = 'Error, reintentar';
            }
        }

        button.addEventListener('click', async () => {
            currentPage++;
            await loadItems(
        }
    };
}