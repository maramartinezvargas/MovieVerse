document.addEventListener("DOMContentLoaded", () => {

    const burger = document.getElementById("burger-btn"
    const nav = document.getElementById("main-nav"

    // Toggle del menú al hacer clic en el botón de hamburguesa
    burger.addEventListener("click", () => {
        burger.classList.toggle("open"
        nav.classList.toggle("open"
    }

    // Cerrar el menú al hacer clic en un enlace
    document.querySelectorAll(".main-nav a").forEach(link=>{
        link.addEventListener("click",()=>{
            burger.classList.remove("open"
            nav.classList.remove("open"
        }
    }

}