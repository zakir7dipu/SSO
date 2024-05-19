const hasError = document.querySelector(".has-error")?.textContent.trim()
const hasSuccess = document.querySelector(".has-success")?.textContent.trim()

if (hasError) toastr.error(hasError);
if (hasSuccess) toastr.success(hasSuccess);