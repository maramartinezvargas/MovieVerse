document.addEventListener('DOMContentLoaded', () => {

    const WHEEL_THROTTLE_MS = 60;
    const lastWheelMap = new WeakMap( // throttle por carousel

    document.querySelectorAll('.carousel-section').forEach(section => {
        const carousel = section.querySelector('.carousel'
        const nextBtn = section.querySelector('.carousel-btn.next'
        const prevBtn = section.querySelector('.carousel-btn.prev'

        // Si no hay cards o botones, se ejecuta el evento del scroll (los botones siguen funcionando pero no hacen nada)
        if (!carousel || !nextBtn || !prevBtn) return;
        const card = carousel.querySelector('.carousel-card'
        if (!card) return;

        const gap = parseInt(getComputedStyle(carousel).gap) || 40;
        const cardWidth = card.offsetWidth + gap;
        const numberCards = 2;
        // Desplazamiento del carousel hacia la derecha
        nextBtn.addEventListener('click', () => {
            carousel.scrollBy({ left: cardWidth * numberCards, behavior: 'smooth' } //smooth = suave
        }

        // Desplazamiento del carousel hacia la izquierda
        prevBtn.addEventListener('click', () => {
            carousel.scrollBy({ left: -cardWidth * numberCards, behavior: 'smooth' }
        }

        // --- Wheel handler (desktop) ---
        const wheelHandler = (e) => {
            // determinar delta predominante
            const dx = e.deltaX || 0;
            const dy = e.deltaY || 0;
            const d = Math.abs(dx) > Math.abs(dy) ? dx : dy;
            if (!d) return;

            const now = Date.now(
            const last = lastWheelMap.get(carousel) || 0;
            if (now - last < WHEEL_THROTTLE_MS) {
                return; // throttle
            }

            const atLeft = carousel.scrollLeft === 0;
            const atRight = Math.ceil(carousel.scrollLeft + carousel.clientWidth) >= carousel.scrollWidth;

            const direction = d > 0 ? 1 : -1;

            if ((direction > 0 && atRight) || (direction < 0 && atLeft)) {
                return; // dejar scroll de página
            }

            e.preventDefault(
            e.stopPropagation(

            carousel.scrollBy({ left: direction * cardWidth * numberCards, behavior: 'smooth' }
            lastWheelMap.set(carousel, now
        };

        carousel.addEventListener('wheel', wheelHandler, { passive: false, capture: true }

        // --- Touch: dejar scroll táctil nativo para móviles para obtener inercia y suavidad ---
        // No añadimos handlers touch personalizados: confiamos en el comportamiento nativo del navegador.

    }

}
