const statusWrapper =
    document.querySelector(".media-status-wrapper"

const statusBtn =
    document.getElementById("statusBtn"

if (statusBtn && statusWrapper) {

    statusBtn.addEventListener("click", (e) => {

        e.stopPropagation(

        statusWrapper.classList.toggle("open"

    }

    document.addEventListener("click", (e) => {

        if (!statusWrapper.contains(e.target)) {
            statusWrapper.classList.remove("open"
        }

    }

}