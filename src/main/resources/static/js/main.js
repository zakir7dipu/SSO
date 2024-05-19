// copyToClipboard
Array.from(document.querySelectorAll(".copy-to-clipboard")).map(item => item.addEventListener("click", () => navigator.clipboard.writeText(item.parentElement.querySelector('input').value)))

const editBtns = document.querySelectorAll(".edit-btn");
Array.from(editBtns).map(item => {

})