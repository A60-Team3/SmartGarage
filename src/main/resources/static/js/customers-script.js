// Confirm delete
function confirmDelete() {
    const clientId = localStorage.getItem('delete_id');

    if (!jwtToken) {
        showAlert('Login to API first.', 'danger');
        return;
    }
    $('#deleteModal').modal('hide');

    // Send DELETE request to delete user
    $.ajax({
        url: `/api/garage/users/${clientId}`,
        type: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + jwtToken
        },
        success: function (response) {
            showAlert('Client deleted successfully', 'success');
            location.reload();  // Reload the page to update the table
        },
        error: function (response) {
            showAlert('Error: ' + response.responseText, 'danger');
        }
    });
}