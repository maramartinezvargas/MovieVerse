/**
 * series.js
 *
 * Inicializa el explorador de series en la página de "Series".
 * Llama a la función global `initExplorer` definida en `explorer.js`.
 *
 * `apiUrl` > endpoint del backend que devuelve JSON paginado.
 *
 *    - apiUrl: endpoint del servidor que devuelve páginas de series en JSON
 *    - gridId: id del contenedor DOM donde se insertarán las tarjetas
 *    - detailBasePath: ruta usada para construir enlaces a la ficha de detalle
 */
document.addEventListener('DOMContentLoaded', () => {
    initExplorer({
        apiUrl: '/api/series',
        gridId: 'series-grid',
        detailBasePath: '/series'
    }
}