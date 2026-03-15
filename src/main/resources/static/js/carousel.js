document.addEventListener('DOMContentLoaded', () => {

    // Para evitar que el scroll con la rueda del ratón se ejecute demasiado rápido se añaden 80ms al evento de scroll
    const WHEEL_THROTTLE_MS = 80;
    // Para almacenar el timestamp del último evento de scroll para cada carousel, y no afectar a otros carouseles al hacer scroll
    const lastWheelMap = new WeakMap( // throttle por carousel

    // Se añade el evento click a cada botón de cada carousel
    document.querySelectorAll('.carousel-section').forEach(section => {
        const carousel = section.querySelector('.carousel'
        const nextBtn = section.querySelector('.carousel-btn.next'
        const prevBtn = section.querySelector('.carousel-btn.prev'

        // Si no se encuentra el carousel o los botones, se omite esta sección
        if (!carousel || !nextBtn || !prevBtn) return;
        const card = carousel.querySelector('.carousel-card'

        // Si no se encuentra ninguna tarjeta dentro del carousel, se omite esta sección
        if (!card) return;

        // Se calcula el número de tarjetas a desplazar al hacer click en los botones, teniendo en cuenta el ancho de las tarjetas y el gap entre ellas
        const gap = parseInt(getComputedStyle(carousel).gap) || 15;
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
