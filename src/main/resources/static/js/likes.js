document.addEventListener("DOMContentLoaded", () => {

    const likeBtn = document.getElementById("likeBtn"

    if (!likeBtn) return;

    const mediaId = likeBtn.dataset.mediaId;
    const mediaType = likeBtn.dataset.mediaType;

    const likeCount = document.getElementById("likeCount"
    const icon = likeBtn.querySelector("i"

    // CSRF (Spring Security)
    const csrfToken = document.querySelector("input[name='_csrf']").value;

    likeBtn.addEventListener("click", async () => {

        try {
            const response = await fetch(`/likes/toggle`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "X-CSRF-TOKEN": csrfToken
                },
                body: new URLSearchParams({
                    mediaId: mediaId,
                    mediaType: mediaType
                })
            }

            const data = await response.json(

            // usuario no logueado
            if (data.error) {
                alert("Tienes que iniciar sesión"
                return;
            }

            // cambiar icono "me gusta"
            if (data.liked) {
                icon.classList.remove("bi-heart"
                icon.classList.add("bi-heart-fill"
                likeBtn.classList.add("liked" //
            } else {
                icon.classList.remove("bi-heart-fill"
                icon.classList.add("bi-heart"
                likeBtn.classList.remove("liked"
            }

            // actualizar contador
            likeCount.textContent = data.totalLikes;

        } catch (error) {
            console.error("Error en like:", error
        }

    }

}