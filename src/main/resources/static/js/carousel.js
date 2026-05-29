/**
* carousel.js — control básico de carruseles horizontales (prev/next scroll).
* Mantener simple: desplaza N tarjetas con scroll suave.
*/
document.addEventListener('DOMContentLoaded', () => {

    document.querySelectorAll('.carousel-section').forEach(section => {
        const carousel = section.querySelector('.carousel'
        const nextBtn = section.querySelector('.carousel-btn.next'
        const prevBtn = section.querySelector('.carousel-btn.prev'

        // Validación: Si falta algún elemento se salta la sección
        if (!carousel || !nextBtn || !prevBtn) return;

        // Se obtiene la tarjeta para calcular el desplazamiento, asumiendo que todas tienen el mismo ancho
        const card = carousel.querySelector('.cast-card') || carousel.querySelector('.carousel-card'
        if (!card) return;

        // Ancho de la tarjeta + espacio entre tarjetas (gap)
        const gap = parseInt(getComputedStyle(carousel).gap) || 40;
        const cardWidth = card.offsetWidth + gap;
        const numberCards = 2;

        // Botón para avanzar (siguiente / -> )
        nextBtn.addEventListener('click', () => {
            carousel.scrollBy({
                left: cardWidth * numberCards,
                behavior: 'smooth'
            }
        }

        // Botón para retroceder (anterior / <- )
        prevBtn.addEventListener('click', () => {
            carousel.scrollBy({
                left: -cardWidth * numberCards,
                behavior: 'smooth'
            }
        }

    }

}