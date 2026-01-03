document.addEventListener('DOMContentLoaded', () => {

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
            carousel.scrollBy({
                left: cardWidth * numberCards,
                behavior: 'smooth'
            }
        }

        prevBtn.addEventListener('click', () => {
            carousel.scrollBy({
                left: -cardWidth * numberCards,
                behavior: 'smooth'
            }
        }
    }

}
