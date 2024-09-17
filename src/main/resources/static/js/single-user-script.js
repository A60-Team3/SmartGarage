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
        passwordConfirm: $('#passwordConfirm').val(),
        profilePic: $('#profilePicture').val()
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
            // Update the UI to reflect new user info
            location.reload(); // Optionally reload the page or just update the UI dynamically
        },
        error: function (xhr, status, error) {
            showAlert('Error updating user: ' + xhr.responseText, 'danger');
        }
    });
    showAlert('User updated successfully!', 'success');
});

function uploadPhoto(){
    document.getElementById('profile-form').submit();
}

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
                                    <td><a href="/garage/visits/${visit.id}">${visit.bookedDate}</a></td>
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
            window.location.href = window.location.origin;
        },
        error: function (response) {
            showAlert('Error: ' + response.responseText, 'danger');
        }
    });
}