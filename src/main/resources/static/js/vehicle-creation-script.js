function filterModels() {
    const selectedBrand = document.getElementById('brand-input').value;
    const brandOptions = document.querySelectorAll('#brands option');
    let selectedBrandId = null;

    // Find the brand id based on selected brand name
    brandOptions.forEach(option => {
        if (option.value === selectedBrand) {
            selectedBrandId = option.getAttribute('data-brand-id');
            console.log(selectedBrandId);
        }
    });

    const modelOptions = document.querySelectorAll('#models option');
    let hasValidModel = false;

    // Filter models based on selected brand
    modelOptions.forEach(option => {
        if (option.getAttribute('data-model-brand-id') === selectedBrand) {
            option = ''; // Show valid options
            hasValidModel = true;
        } else {
            option.style.display = 'none'; // Hide non-matching options
        }
    });

    // Show or hide the model input based on available models
    if (hasValidModel) {
        document.getElementById('model-input').style.display = 'block';
    } else {
        document.getElementById('model-input').style.display = 'none';
        document.getElementById('year-input').style.display = 'none'; // Hide year if no model is selected
    }
}

function filterYears() {
    const selectedModel = document.getElementById('model').value;
    const modelOptions = document.querySelectorAll('#models option');
    let selectedModelId = null;

    // Find the model id based on selected model name
    modelOptions.forEach(option => {
        if (option.value === selectedModel) {
            selectedModelId = option.getAttribute('data-model-id');
        }
    });

    const yearOptions = document.querySelectorAll('#years option');
    let hasValidYear = false;

    // Filter years based on selected model
    yearOptions.forEach(option => {
        if (option.getAttribute('data-model-id') === selectedModelId) {
            option.style.display = ''; // Show valid options
            hasValidYear = true;
        } else {
            option.style.display = 'none'; // Hide non-matching options
        }
    });

    // Show or hide the year input based on available years
    if (hasValidYear) {
        document.getElementById('year').style.display = 'block';
    } else {
        document.getElementById('year').style.display = 'none';
    }
}

