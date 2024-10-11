document.getElementById('signupForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the form from submitting normally

    // Collect form data and send it via fetch
    const userData = {
        username: document.getElementById('username').value.trim(),
        firstName: document.getElementById('firstName').value.trim(),
        lastName: document.getElementById('lastName').value.trim(),
        email: document.getElementById('email').value.trim(),
        emailVerified: true,
        enabled: true,
        password: document.getElementById('password').value.trim()
    };

    fetch('/signup', { // Make sure to include the trailing slash
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' // Specify JSON content type
        },
        body: JSON.stringify(userData)
    }).then(response => {
        if (response.ok) { // Check if response status is in the range 200-299
            // Redirect to Keycloak login page or handle success here
            window.location.href = 'http://localhost:8080/realms/Messenger/protocol/openid-connect/auth?response_type=code&client_id=chat-messenger&scope=openid%20profile%20roles&redirect_uri=http://localhost:8020/login/oauth2/code/keycloak';// Redirect after successful signup
        } else if (response.status === 409) {
            throw new Error('Conflict: User could not be created.');
        } else {
            throw new Error('Error registering user.');
        }
    })
        .catch((error) => {
            console.error('Error:', error);
            alert(error.message); // Display error message to user
        });
});