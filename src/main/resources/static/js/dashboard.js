// src/main/resources/static/js/dashboard.js
/**
 * dashboard.js
 * Propósito: paginar la lista de reportes en el dashboard.
 * - Muestra inicialmente un número fijo de filas (STEP).
 * - Al pulsar el botón "Cargar más" muestra las siguientes STEP filas.
 * - Oculta el botón cuando no quedan más filas.
 *
 * No requiere cambios en el servidor. Comentarios en español para mantener
 * consistencia con otros .js del proyecto.
 */

document.addEventListener('DOMContentLoaded', () => {

    // Número de filas a mostrar por "página"
    const STEP = 4;
    // Todas las filas de reporte en el DOM
    const rows = Array.from(document.querySelectorAll('.report-row')
    // Botón que activa la carga de más filas
    const button = document.getElementById('load-more-reports-btn'

    // Si no hay filas o no existe el botón, salir sin hacer nada
    if (!rows.length || !button) {
        return;
    }

    // Contador de filas actualmente visibles
    let visibleRows = STEP;

    // Si el total de filas es menor o igual al STEP inicial, deshabilitar el botón y cambiar su texto
    if (rows.length <= STEP) {
        button.disabled = true;
        button.innerHTML = '<i class="bi bi-check-circle"></i> No hay más reportes';
    }

    // Ocultar las filas que exceden el primer bloque (STEP)
    rows.forEach((row, index) => {
        if (index >= STEP) {
            row.style.display = 'none';
        }
    }

    // Al pulsar el botón, mostrar el siguiente bloque de filas
    button.addEventListener('click', () => {

        const nextLimit = visibleRows + STEP;

        // Mostrar filas desde visibleRows (incluido) hasta nextLimit (excluido)
        rows.forEach((row, index) => {
            if (index >= visibleRows && index < nextLimit) {
                row.style.display = '';
            }
        }

        // Actualizar contador de visibles
        visibleRows = nextLimit;

        // Si ya mostramos todas las filas, deshabilitar el botón y cambiar su texto
        if (visibleRows >= rows.length) {
            button.disabled = true;
            button.innerHTML = '<i class="bi bi-check-circle"></i> No hay más reportes';
        }
    }

}
