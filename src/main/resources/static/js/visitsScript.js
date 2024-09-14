$(document).ready(function () {
    const scheduleOption = document.getElementById('scheduleOption');
    const scheduleEndContainer = document.getElementById('scheduleEndContainer');

    if (scheduleOption != null) {
        if (scheduleOption.value === 'BETWEEN') {
            scheduleEndContainer.style.display = 'block';
        } else {
            scheduleEndContainer.style.display = 'none';
        }
    }
});

function toggleScheduleEnd() {
    const scheduleOption = document.getElementById('scheduleOption');
    const scheduleEndContainer = document.getElementById('scheduleEndContainer');

    if (scheduleOption != null) {
        if (scheduleOption.value === 'BETWEEN') {
            scheduleEndContainer.style.display = 'block';
        } else {
            scheduleEndContainer.style.display = 'none';
        }
    }
}

// Submit the reschedule request
function submitReschedule() {
    const visitId = localStorage.getItem('visit_id');
    const newBookedDate = document.getElementById('newBookedDate').value;
    $('#rescheduleModal').modal('hide');

    if (!jwtToken) {
        showAlert('Login to API first.', 'danger');
        return;
    }

    // Send PUT request to update visit
    $.ajax({
        url: `/api/garage/visits/${visitId}`,
        type: 'PUT',
        headers: {
            'Authorization': 'Bearer ' + jwtToken
        },
        data: { bookedDate: newBookedDate },
        success: function(response) {
            window.location.href = `${window.location}&rescheduled=true&rescheduleVisitId=${visitId}`;
        },
        error: function(response) {
            showAlert('Error: ' + response.responseText, 'danger');
        }
    });
    showAlert('Visit rescheduled successfully', 'success');

}

// Confirm delete
function confirmDelete() {
    const visitId = localStorage.getItem('delete_id');

    if (!jwtToken) {
        showAlert('Login to API first.', 'danger');
        return;
    }
    $('#deleteModal').modal('hide');

    // Send DELETE request to cancel visit
    $.ajax({
        url: `/api/garage/visits/${visitId}`,
        type: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + jwtToken
        },
        success: function (response) {
            showAlert('Visit cancelled successfully', 'success');
            location.reload();  // Reload the page to update the table
        },
        error: function (response) {
            showAlert('Error: ' + response.responseText, 'danger');
        }
    });
}

$(document).ready(function() {
    const urlParams = new URLSearchParams(window.location.search);
    const updatedVisitId = urlParams.get('rescheduleVisitId');

    if (updatedVisitId) {
        const visitRow = document.getElementById('visit-row-' + updatedVisitId);
        if (visitRow) {
            // Scroll to the updated visit row
            visitRow.scrollIntoView({ behavior: 'smooth', block: 'center' });
            // Optionally highlight the row for better visibility
            $(visitRow).css('background-color', '#dede9c');
            setTimeout(() => {
                $(visitRow).css('background-color', '');
            }, 2000); // Reset the background color after 2 seconds
        }
    }
});

$('#rescheduleModal').on('show.bs.modal', function (event) {
    const button = $(event.relatedTarget);
    const vehicleId = button.data('visit-id');
    localStorage.setItem('visit_id', vehicleId);
});

$('#rescheduleModal').on('hidden.bs.modal', function (event) {
    localStorage.removeItem('visit_id');
});


