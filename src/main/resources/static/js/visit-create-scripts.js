function filterVehicles() {
    const selectedOwner = document.getElementById('client-input').value;
    const modelOptions = document.querySelectorAll('#vehicles option');
    document.getElementById('vehicles').selectedIndex = -1;

    // Filter models based on selected brand
    if (selectedOwner !== '') {
        modelOptions.forEach(option => {
            if (option.getAttribute('data-owner-id') === selectedOwner) {
                option.disabled = false;
                option.style.display = 'block';
            } else {
                option.disabled = true;
                option.style.display = 'none';
            }
        });
    } else {
        modelOptions.forEach(option => {
            option.style.display = 'block';
            option.disabled = true;
        });
    }
}

document.addEventListener("DOMContentLoaded", function () {
    // Disable past dates by setting the minimum date to today
    const today = new Date().toISOString().split('T')[0];

    flatpickr("#bookingDate", {
        dateFormat: "Y-m-d", // Date format YYYY-MM-DD
        minDate: today, // Disable past dates
        disable: fullyBookedDates, // Disable fully booked dates
        onDayCreate: function (dObj, dStr, fp, dayElem) {
            const date = dayElem.dateObj.toISOString().split('T')[0]; // Get date in YYYY-MM-DD format

            if (fullyBookedDates.includes(date)) {
                dayElem.classList.add("fully-booked"); // Add custom class to highlight fully booked dates
            }
        }
    });
});