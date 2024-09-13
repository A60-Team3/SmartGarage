$(document).ready(function() {
    const scheduleOption = document.getElementById('scheduleOption');
    const scheduleEndContainer = document.getElementById('scheduleEndContainer');

    if (scheduleOption.value === 'BETWEEN') {
        scheduleEndContainer.style.display = 'block';
    } else {
        scheduleEndContainer.style.display = 'none';
    }
});

function toggleScheduleEnd() {
    const scheduleOption = document.getElementById('scheduleOption');
    const scheduleEndContainer = document.getElementById('scheduleEndContainer');

    if (scheduleOption.value === 'BETWEEN') {
        scheduleEndContainer.style.display = 'block';
    } else {
        scheduleEndContainer.style.display = 'none';
    }
}

function toggleFilter() {
    const filterFormContainer = document.getElementById('filter-form-container');
    const isVisible = window.getComputedStyle(filterFormContainer).display !== "none";

    filterFormContainer.style.display = isVisible ? 'none' : 'block';
}
