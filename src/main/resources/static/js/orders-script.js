function generateOrderInputs() {
    var orderCount = document.getElementById('orderCount').value;
    var orderInputsContainer = document.getElementById('orderInputs');

    // Clear the container before generating new inputs
    orderInputsContainer.innerHTML = '';

    // Generate inputs for each order
    for (var i = 0; i < orderCount; i++) {
        var inputGroup = document.createElement('div');

        // Create label for order type ID
        var label = document.createElement('label');
        label.textContent = 'Order ' + (i + 1) + ' - Service Type ID:';
        inputGroup.appendChild(label);

        // Create input field for serviceTypeId
        var input = document.createElement('input');
        //input.setAttribute('type', 'text');
        input.setAttribute('list', 'serviceTypeList');  // Associate with the datalist
        input.setAttribute('name', 'serviceTypeIds');
        input.setAttribute('id', 'serviceTypeIds');
        input.setAttribute('required', 'true');
        input.setAttribute('class', 'form-control');
        input.setAttribute('style', 'height: 55px;')
        input.setAttribute('oninput', 'filterServices()')
        inputGroup.appendChild(input);

        // Add the input group to the container
        orderInputsContainer.appendChild(inputGroup);
    }
}
var array = [];

function filterServices() {
    let selectedService = document.getElementById('serviceTypeIds').value;
    const serviceOptions = document.querySelectorAll('#serviceTypeList option');
    array.push(selectedService);
    console.log(array)
    // Filter models based on selected brand
    serviceOptions.forEach(option => {
        if (array.includes(option.getAttribute('data-order-type-id'))) {
            option.disabled = true;// Show valid options
        } else {
            option.disabled = false;
        }
    });
}
// Generate initial input field on page load
document.addEventListener('DOMContentLoaded', generateOrderInputs);