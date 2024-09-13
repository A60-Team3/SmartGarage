$(document).ready(function () {

    // Search form submission handler
    $('#vehicle-search-form').submit(function (event) {
        event.preventDefault();

        const licensePlate = $('#licensePlate').val();
        const vin = $('#vin').val();
        const brand = $('#brand').val();

        if (!jwtToken) {
            console.log('User is not logged in.');
            return;
        }

        // AJAX call to search vehicles
        $.ajax({
            url: '/api/garage/vehicles/vin',
            type: 'GET',
            headers: {
                'Authorization': 'Bearer ' + jwtToken
            },
            data: {
                licensePlate: licensePlate,
                vin: vin,
                brandName: brand
            },
            success: function (response) {
                $('#vehicle-list').empty(); // Clear any existing data
                $('#vehicle-list-section').show();
                $('#visit-info-section').hide();

                if (response.length === 0) {
                    $('#vehicle-list').append('<li>No vehicles found</li>');
                } else {
                    response.forEach(vehicle => {
                        $('#vehicle-list').append(`<li><a href="#" onclick="fetchVisits(${vehicle.id})">${vehicle.brandName} ${vehicle.licensePlate}</a></li>`);
                    });
                }
            },
            error: function () {
                console.log('Failed to search vehicles');
            }
        });
    });

    // Fetch visits for selected vehicle
    window.fetchVisits = function (vehicleId) {

        $.ajax({
            url: '/api/garage/clerks/visits',
            type: 'GET',
            headers: {
                'Authorization': 'Bearer ' + jwtToken
            },
            data: {
                vehicleId: vehicleId
            },
            success: function (response) {
                $('#visit-info-body').empty(); // Clear existing visits
                $('#visit-info-section').show();

                response.content.forEach(visit => {
                    const visitRow = `
                                <tr>
                                    <td>${visit.bookedDate}</td>
                                    <td>${visit.clientName}</td>
                                    <td>
                                        <select id="status-${visit.id}">
                                            <option value="NOT_STARTED" ${visit.status === 'NOT_STARTED' ? 'selected' : ''}>NOT STARTED</option>
                                            <option value="IN_PROGRESS" ${visit.status === 'IN_PROGRESS' ? 'selected' : ''}>IN PROGRESS</option>
                                            <option value="READY_FOR_PICKUP" ${visit.status === 'READY_FOR_PICKUP' ? 'selected' : ''}>READY FOR PICKUP</option>
                                        </select>
                                    </td>
                                    <td>${visit.services.map(service => service.serviceName).join(', ')}</td>
                                    <td><button onclick="updateVisit(${visit.id})">Update</button></td>
                                </tr>
                            `;
                    $('#visit-info-body').append(visitRow);
                });
            },
            error: function () {
                console.log('Failed to fetch visits');
            }
        });
    };

    // Update visit status
    window.updateVisit = function (visitId) {
        const status = $(`#status-${visitId}`).val();

        $.ajax({
            url: `/api/garage/visits/${visitId}`,
            type: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + jwtToken
            },
            data: {
                status: status
            },
            success: function () {
                alert('Visit updated successfully');
            },
            error: function () {
                alert('Failed to update visit');
            }
        });
    };
});