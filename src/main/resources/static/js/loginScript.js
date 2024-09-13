document.querySelector('.garage-door-form').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent the default form submission

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    loginUser(username, password);

    const form = document.querySelector('.garage-door-form');
    const formToHide = document.querySelector('.door-form');
    const passwordReset = document.querySelector('.password-reset');
    const blackVoid = document.querySelector('.black-void');
    const messages = document.querySelector('.login-messages');

    form.classList.remove('rolling-up', 'rolling-down');

    // Create the request payload
    const payload = new URLSearchParams();
    payload.append('username', username);
    payload.append('password', password);

    // Send the login request to the server
    fetch('/garage/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
        },
        body: payload
    })
        .then(response => {

            if (!response.ok) {
                // The response status is not OK, throw an error to handle in the catch block
                throw new Error('Login failed with status: ' + response.status);
            }

            const url = response.url;
            passwordReset.style.display = "none";
            formToHide.style.display = "none";
            messages.style.display = "none";
            form.classList.add('rolling-up');
            if (url.endsWith("error")) {
                failLogin(form, url,messages, passwordReset, formToHide);
            } else {
                successLogin(form, blackVoid, url);
            }
        });
});

function failLogin(form, url, messages, passwordReset, formToHide) {
    setTimeout(() => {
        form.classList.remove('rolling-up');
        document.getElementById('username').value = '';
        document.getElementById('password').value = '';
        form.classList.add('rolling-down');
        setTimeout(() => {
            const errorMessage = document.createElement("h5");
            errorMessage.classList.add('error-message')
            const newMessage = document.createTextNode("Invalid username or password");
            errorMessage.appendChild(newMessage);
            messages.appendChild(errorMessage);

            formToHide.style.display = "flex";
            messages.style.display = "flex";
            passwordReset.style.display = "flex";
        }, 3000); // Time to roll door down
    }, 3000); // Wait for door to roll up
}

function successLogin(form, blackVoid, url) {
    // Once the door is fully open, apply the sucking effect
    setTimeout(() => {
        blackVoid.classList.add('suck-inside');

        // Redirect after sucking effect is done
        setTimeout(() => {
            window.location.href = url; // Change this to your redirect page
        }, 2000);

    }, 3000); // Wait for door to fully roll up
}

function loginUser(username, password) {
    $.ajax({
        url: '/api/garage/login',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            username: username,
            password: password
        }),
        success: function (response) {
            jwtToken = response.token;

            localStorage.setItem('jwtToken', jwtToken);

            console.log('Login successful, JWT token stored.');
        },
        error: function () {
            console.log('Failed to log in. Please check your credentials.');
        }
    });
}