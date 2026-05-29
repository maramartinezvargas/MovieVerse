/**
 * movies.js
 *
 * Inicializa el explorador de películas en la página de "Películas".
 * Llama a la función global `initExplorer` definida en `explorer.js`.
 *
 * Nota: `apiUrl` debe ser un endpoint del backend que devuelve JSON paginado
 * (por ejemplo '/api/peliculas'). El backend es el que consulta TMDB. No se
 * llama directamente a la API pública de TMDB desde el navegador.
 *
 *     // - apiUrl: endpoint del servidor que devuelve páginas de películas en JSON
 *     // - gridId: id del contenedor DOM donde se insertarán las tarjetas
 *     // - detailBasePath: ruta usada para construir enlaces a la ficha de detalle
 */
document.addEventListener('DOMContentLoaded', () => {
    initExplorer({
        apiUrl: '/api/peliculas',
        gridId: 'movies-grid',
        detailBasePath: '/peliculas'
    }
}