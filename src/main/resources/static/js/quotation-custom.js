function addService() {
    const serviceContainer = document.getElementById('services-container');
    const serviceEntry = document.querySelector('.service-entry').cloneNode(true);

    // serviceEntry.querySelector('select').selectedIndex = 0;

    serviceContainer.appendChild(serviceEntry);
}

document.addEventListener("DOMContentLoaded", function() {
    // Check if the 'email' parameter is present in the URL
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('sent')) {
        // Trigger the modal if 'email' is present
        const modal = new bootstrap.Modal(document.getElementById('successModal'));
        modal.show();
    }
});