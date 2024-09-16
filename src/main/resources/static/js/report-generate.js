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