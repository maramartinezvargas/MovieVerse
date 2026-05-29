/**
* reportModal.js
*
* Abrir/ceder modal de revisión de la review desde el panel de moderación.
* - `openReviewModal(button)` solicita al backend la asignación/estado y carga la review.
* - Actualiza badges y contadores del dashboard.
* - Provee funciones para cerrar el modal (clic fuera o ESC).
*
*/
async function openReviewModal(button) {

    try {
        const reviewId = button.dataset.reviewId;
        const reportId = button.dataset.reportId;

        // Llamada al backend que:
        // - asigna el moderador al reporte
        // - cambia el estado del reporte a UNDER_REVIEW (persistente)
        // - valida permisos
        await fetch(`/reports/${reportId}/review`

        // Actualizar badge UI en la fila correspondiente
        const row = button.closest('tr'
        const badge = row.querySelector('.status-badge'


        // Si no estaba ya en UNDER_REVIEW, actualizar texto/clases y contadores
        if (badge && !badge.classList.contains('under_review')) {

            badge.textContent = 'UNDER_REVIEW';

            badge.classList.remove(
                'pending',
                'resolved',
                'rejected'
            

            badge.classList.add('under_review'

            // Actualizar contador en el dashboard (si existen los elementos)
            const pendingCount = document.getElementById('pendingCount'
            const underReviewCount = document.getElementById('underReviewCount'

            if (pendingCount && underReviewCount) {
                    // Decrementar pendientes (sin bajar de 0) y aumentar en revisión
                    pendingCount.textContent =
                        Math.max(
                            0,
                            parseInt(pendingCount.textContent) - 1
                        

                underReviewCount.textContent = parseInt(underReviewCount.textContent) + 1;
            }
        }

        // Obtener la review concreta (JSON) para mostrar en el modal
        const response = await fetch(`/reviews/${reviewId}`

        if (!response.ok) {
            throw new Error(
                'No se pudo cargar la review'
            
        }

        const review = await response.json(

        document.getElementById(
            'dashboardReviewContent'
        ).innerHTML = `

            <div class="review-modal-meta">

                <div class="review-meta-pill">
                    <i class="bi bi-person-fill"></i>
                    ${review.username}
                </div>

                <div class="review-meta-pill">
                    <i class="bi bi-star-fill"></i>
                    ${review.rating}/10
                </div>

                <div class="review-meta-pill">
                    <i class="bi bi-calendar-event"></i>
                    ${new Date(review.createdAt)
            .toLocaleDateString('es-ES')}
                </div>

            </div>

            <div class="review-comment">
                ${review.comment}
            </div>

        `;

        document.getElementById(
            'dashboardReviewModal'
        ).style.display = 'flex';

    } catch (error) {

        console.error(error

        alert('Error al cargar la review.'
    }
}

function closeDashboardReviewModal() {

    document.getElementById(
        'dashboardReviewModal'
    ).style.display = 'none';
}

// click fuera modal
window.addEventListener('click', function(event) {

    const modal =
        document.getElementById('dashboardReviewModal'

    if (event.target === modal) {

        closeDashboardReviewModal(
    }
}

// ESC
document.addEventListener('keydown', function(event) {

    if (event.key === 'Escape') {

        closeDashboardReviewModal(
    }
}