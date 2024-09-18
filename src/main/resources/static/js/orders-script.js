var array = new Array(document.getElementById('serviceTypeList').getAttribute('size')).fill('');
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
        input.setAttribute('id', 'serviceTypeIds[' + i + ']');
        input.setAttribute('autocomplete', 'off');
        input.setAttribute('required', 'true');
        input.setAttribute('class', 'form-control');
        input.setAttribute('style', 'height: 55px;')
        input.setAttribute('oninput', 'filterServices('+i+')')

        if (i < array.length) {
            if(array[i] === undefined){
                input.value = '';
            } else {
                input.value = array[i];
            }
        }
        inputGroup.appendChild(input);
        // Add the input group to the container
        orderInputsContainer.appendChild(inputGroup);
    }
}


function filterServices(i) {

    let selectedService = document.getElementById('serviceTypeIds['+i+']').value;
    const serviceOptions = document.querySelectorAll('#serviceTypeList option');


    array[i] = selectedService;
    console.log(array);
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