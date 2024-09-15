$('#editUserForm').submit(function (event) {
    event.preventDefault();

    if (!jwtToken) {
        showAlert('Login to API first.', 'danger');
        return;
    }

    const userId = $('#userId').val();
    const userData = {
        firstName: $('#firstName').val(),
        lastName: $('#lastName').val(),
        username: $('#username').val(),
        email: $('#email').val(),
        phoneNumber: $('#phoneNumber').val(),
        password: $('#password').val(),
        passwordConfirm: $('#passwordConfirm').val()
    };

    $.ajax({
        url: `/api/garage/users/${userId}`,
        type: 'PUT',
        headers: {
            'Authorization': 'Bearer ' + jwtToken
        },
        contentType: 'application/json',
        data: JSON.stringify(userData),
        success: function (response) {
            $('#editUserModal').modal('hide');
            showAlert('User updated successfully!', 'success');
            // Update the UI to reflect new user info
            location.reload(); // Optionally reload the page or just update the UI dynamically
        },
        error: function (xhr, status, error) {
            showAlert('Error updating user: ' + xhr.responseText, 'danger');
        }
    });
});

$('#carsBtn').click(function () {
    const userId = $('#userId').val();

    $.ajax({
        url: `/api/garage/vehicles/user/${userId}`,
        type: 'GET',
        headers: {
            'Authorization': 'Bearer ' + jwtToken
        },
        success: function (response) {
            $('#vehicle-list').empty(); // Clear any existing data
            $('#vehicle-list-section').show();

            if (response.length === 0) {
                $('#vehicle-list').append('<li>No vehicles found</li>');
            } else {
                response.forEach(vehicle => {
                    $('#vehicle-list').append(`
                    <li class="list-unstyled">
                        <div>
                            <h5>License Plate: 
                            <button class="vehicle-license-plate btn" type="button" value="${vehicle.id}"
                            data-vehicle-id="${vehicle.id}">
                                ${vehicle.licensePlate}
                            </button>
                            </h5>
                            <h6>Brand: ${vehicle.brandName}</h6>
                            <h6>Model: ${vehicle.modelName}</h6>
                        </div>
                    </li>`);
                });
            }
        },
        error: function (xhr, status, error) {
            showAlert('Error fetching user cars: ' + xhr.responseText, 'danger');
        }
    });
});

$(document).on('click', '#visitsBtn, .vehicle-license-plate', function () {
    const userId = $('#userId').val();
    let vehicleId = null;
    if ($(this).hasClass('vehicle-license-plate')) {
        vehicleId = $(this).data('vehicle-id');
    }

    $.ajax({
        url: `/api/garage/users/${userId}/visits`,
        type: 'GET',
        headers: {
            'Authorization': 'Bearer ' + jwtToken
        },
        data: {vehicleId: vehicleId},
        success: function (response) {
            $('#visit-info-body').empty(); // Clear existing visits
            $('#user-visits-info').show();

            response.content.forEach(visit => {
                const visitRow = `
                                <tr>
                                    <td>${visit.bookedDate}</td>
                                    <td><a href="/garage/vehicles/${visit.vehicle.id}">${visit.vehicle.licensePlate}</a></td>
                                    <td>${visit.status}</td>
                                    <td>${visit.employeeName}</td>
                                    <td>${visit.totalCost} ${visit.currency}</td>
                                    <td>
                                        <input type="checkbox" value="${visit.id}" id="report-{$visit.id}">
                                    </td>
                                </tr>
                            `;
                $('#visit-info-body').append(visitRow);
            });

        },
        error: function (xhr, status, error) {
            showAlert('Error fetching user visits: ' + xhr.responseText, 'danger');
        }
    });
});


// Function to check if any checkbox is checked
function checkVisitsSelected() {
    const isAnyChecked = $('input[type=checkbox]:checked').length > 0;
    $('#reportBtn').prop('disabled', !isAnyChecked);  // Enable if at least one is checked, disable otherwise
}

// Call this function whenever a checkbox is clicked
$(document).on('change', 'input[type=checkbox]', function() {
    checkVisitsSelected();  // Check for selected visits whenever a checkbox state changes
});

// Initial call to disable the button if no checkboxes are checked initially
$(document).ready(function() {
    checkVisitsSelected();  // Initial check when the page loads
});


$('#generateReportForm').submit(function (event) {
    event.preventDefault();

    if (!jwtToken) {
        showAlert('Login to API first.', 'danger');
        return;
    }

    const selectedVisitIds = [];
    $('input[type=checkbox]:checked').each(function () {
        selectedVisitIds.push($(this).val());
    });

    const targetEmail = $('#targetEmail').val();
    const exchangeCurrency = $('#exchangeCurrency').val();
    $('#generateReportModal').modal('hide');

    $.ajax({
        url: '/api/garage/visits/report',
        type: 'GET',
        headers: {
            'Authorization': 'Bearer ' + jwtToken
        },
        data: {
            visitIds: selectedVisitIds,
            email: targetEmail,
            exchangeCurrency: exchangeCurrency
        },
        success: function (response) {
            showAlert('Report generated and sent to ' + targetEmail, 'success');
        },
        error: function (xhr, status, error) {
            showAlert('Error generating report: ' + xhr.responseText, 'danger');
        }
    });
});

function confirmDelete() {
    const userId = localStorage.getItem('delete_id');

    if (!jwtToken) {
        showAlert('Login to API first.', 'danger');
        return;
    }
    $('#deleteModal').modal('hide');

    // Send DELETE request to cancel visit
    $.ajax({
        url: `/api/garage/users/${userId}`,
        type: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + jwtToken
        },
        success: function (response) {
            showAlert('User deleted successfully', 'success');
            location.reload();  // Reload the page to update the table
        },
        error: function (response) {
            showAlert('Error: ' + response.responseText, 'danger');
        }
    });
}