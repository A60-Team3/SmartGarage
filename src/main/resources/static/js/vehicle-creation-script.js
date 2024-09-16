function filterModels() {
    const selectedBrand = document.getElementById('brand-input').value;
    const modelOptions = document.querySelectorAll('#models option');

    // Filter models based on selected brand
    modelOptions.forEach(option => {
        if (option.getAttribute('data-model-brand') === selectedBrand) {
            option.disabled = false;// Show valid options
        } else {
           option.disabled = true;
        }
    });
}

function filterYears() {
    const selectedModel = document.getElementById('model-input').value;

    const yearOptions = document.querySelectorAll('#years option');

    // Filter years based on selected model
    yearOptions.forEach(option => {
        if (option.getAttribute('data-model-years').includes(selectedModel)) {
            option.disabled = false; // Show valid options
        } else {
            option.disabled = true; // Hide non-matching options
        }
    });
}

