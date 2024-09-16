let jwtToken = null;

$(document).ready(function() {
    jwtToken = localStorage.getItem('jwtToken');
});

// Display success message
function showAlert(message, type) {
    const alertPlaceholder = document.getElementById('alert-placeholder');
    const alertDiv = document.createElement('div');
    alertDiv.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;

    alertPlaceholder.appendChild(alertDiv);

    setTimeout(() => {
        $(alertDiv).alert('close');
    }, 5000);
}

function toggleFilter() {
    const filterFormContainer = document.getElementById('filter-form-container');
    const isVisible = window.getComputedStyle(filterFormContainer).display !== "none";

    filterFormContainer.style.display = isVisible ? 'none' : 'block';
}

$('#deleteModal').on('show.bs.modal', function (event) {
    const button = $(event.relatedTarget);
    const vehicleId = button.data('delete-id');
    localStorage.setItem('delete_id', vehicleId);
});

$('#deleteModal').on('hidden.bs.modal', function (event) {
    localStorage.removeItem('delete_id');
});

function profilePage(){
    const username = document.getElementById('profilePage');

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
}