document.addEventListener("DOMContentLoaded", function () {
    // Get the current path and query string
    const currentPage = window.location.pathname
    // Select all the nav-item links and dropdown items
    const navLinks = document.querySelectorAll('.navbar-nav .nav-item.nav-link');
    const dropdownItems = document.querySelectorAll('.dropdown-menu .dropdown-item');

    // Function to check and add 'active' class if href matches current URL
    function setActiveLink(links) {
        links.forEach(function (link) {
            if (link.getAttribute("href") === currentPage) {
                link.classList.add("active");
            }
        });
    }

    // Apply the function to both nav-links and dropdown items
    setActiveLink(navLinks);
    setActiveLink(dropdownItems);

    // If a dropdown item is active, also activate the parent dropdown toggle
    const activeDropdownItem = document.querySelector('.dropdown-item.active');
    if (activeDropdownItem) {
        const dropdownToggle = activeDropdownItem.closest('.nav-item').querySelector('.dropdown-toggle');
        if (dropdownToggle) {
            dropdownToggle.classList.add("active");
        }
    }
});

// On href click, it will choose a random you tube video from a list

function getRandomYoutubeUrl() {
    const youtubeUrls = [
        'https://youtu.be/rd-0LtV5Axo',
        'https://youtu.be/UAKCR7kQMTQ',
        'https://youtu.be/Y1D3a5eDJIs',
        'https://youtu.be/t6OS_ItMGpc',
        'https://youtu.be/PvF9PAxe5Ng'
    ];

    const randomIndex = Math.floor(Math.random() * youtubeUrls.length);
    this.href = youtubeUrls[randomIndex];
}

document.addEventListener("DOMContentLoaded", function () {
    const youtubeLink = document.getElementById('youtube-link');
    youtubeLink.addEventListener('click', getRandomYoutubeUrl);
});

function logout() {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/garage/logout';

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfInput = document.createElement('input');
    csrfInput.type = 'hidden';
    csrfInput.name = '_csrf';
    csrfInput.value = csrfToken;
    form.appendChild(csrfInput);

    localStorage.clear();

    document.body.appendChild(form);
    form.submit();
}