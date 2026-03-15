document.addEventListener('DOMContentLoaded', () => {

    const WHEEL_THROTTLE_MS = 200;
    const lastWheelMap = new WeakMap( // throttle por carousel

    document.querySelectorAll('.carousel-section').forEach(section => {

        const carousel = section.querySelector('.carousel'
        const nextBtn = section.querySelector('.carousel-btn.next'
        const prevBtn = section.querySelector('.carousel-btn.prev'

        if (!carousel || !nextBtn || !prevBtn) return;

        const card = carousel.querySelector('.carousel-card'
        if (!card) return;

        const gap = parseInt(getComputedStyle(carousel).gap) || 30;
        const cardWidth = card.offsetWidth + gap;
        const numberCards = 2;

        nextBtn.addEventListener('click', () => {
            carousel.scrollBy({ left: cardWidth * numberCards, behavior: 'smooth' }
        }

        prevBtn.addEventListener('click', () => {
            carousel.scrollBy({ left: -cardWidth * numberCards, behavior: 'smooth' }
        }

        // Manejar el scroll con la rueda del ratón para scrollear horizontalmente en los carouseles
        const wheelHandler = (e) => {
            const dx = e.deltaX || 0;
            const dy = e.deltaY || 0;
            const d = Math.abs(dx) > Math.abs(dy) ? dx : dy;
            if (!d) return;
            const now = Date.now(
            const last = lastWheelMap.get(carousel) || 0;
            if (now - last < WHEEL_THROTTLE_MS) {
                return; // throttle: permitir que el navegador procese el evento
            }
            const atLeft = carousel.scrollLeft === 0;
            const atRight = Math.ceil(carousel.scrollLeft + carousel.clientWidth) >= carousel.scrollWidth;
            const direction = d > 0 ? 1 : -1; // 1 => derecha, -1 => izquierda
            if ((direction > 0 && atRight) || (direction < 0 && atLeft)) {
                return;
            }
            e.preventDefault( // para evitar scroll de página mientras se scrollea por el carousel
            e.stopPropagation(
            carousel.scrollBy({ left: direction * cardWidth * numberCards, behavior: 'smooth' }
            lastWheelMap.set(carousel, now
        };
        carousel.addEventListener('wheel', wheelHandler, { passive: false, capture: true }
    }

}
