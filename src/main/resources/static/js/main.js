let isProfileOpen = false;

document.getElementById('profileButton').onclick = function() {
    const profileTab = document.getElementById('profileTab');

    if (isProfileOpen) {
        profileTab.classList.add('hide');
        setTimeout(() => {
            profileTab.style.display = 'none';
            profileTab.classList.remove('hide');
        }, 300);
    } else {
        profileTab.style.display = 'block';
        profileTab.classList.add('show');
        // Close the tab when clicking outside
        document.addEventListener('click', function(event) {
            if (!profileTab.contains(event.target) && event.target !== document.getElementById('profileButton')) {
                closeProfile();
            }
        });
    }

    isProfileOpen = !isProfileOpen;
};

function closeProfile() {
    const profileTab = document.getElementById('profileTab');

    profileTab.classList.add('hide');

    setTimeout(() => {
        profileTab.style.display = 'none';
        profileTab.classList.remove('hide');
    }, 300);

    isProfileOpen = false;
}

// This function will be called to set the logged-in user's name
function setUsername(username) {
    document.getElementById('loggedInUser').innerText = username; // Set username dynamically
}

// Fetch active users from the backend
fetch('/main/') // Call the endpoint to get online users
    .then(response => response.json())
    .then(users => updateUserList(users))
    .catch(error => console.error('Error fetching users:', error)); // Handle any errors

// Function to update the user list on the page
function updateUserList(users) {
    const userListDiv = document.getElementById('userList');
    userListDiv.innerHTML = ''; // Clear existing list

    users.forEach(user => {
        const li = document.createElement('li');
        li.classList.add('user-box');

        li.innerHTML = `
             <div class="user-info">
                 <i class="fas fa-user-circle user-icon"></i>
                 <span class="user-name">${user}</span>
             </div>
             <i class="fas fa-times close-icon" onclick="removeUser('${user}')"></i>
         `;

        li.onclick = () => openChat(user); // Open chat on click

        userListDiv.appendChild(li);
    });
}

function removeUser(user) {
    alert(`Remove functionality for ${user} goes here.`); // Placeholder for remove functionality
}

// Function to filter users based on search input
function filterUsers() {
    const input = document.getElementById('searchInput').value.toLowerCase();
    const userListDiv = document.getElementById('userList');
    const users = userListDiv.getElementsByTagName('li');

    Array.from(users).forEach(user => {
        const text = user.innerText.toLowerCase();
        user.style.display = text.includes(input) ? '' : 'none'; // Show or hide based on input
    });
}

// Function to open chat with selected recipient
function openChat(receiver) {
    const sender = document.getElementById('loggedInUser').innerText;

    const url = `/chat?sender=${sender}&receiver=${receiver}`;

    window.location.href = url; // Redirect to chat page URL
}
const pathSegments = window.location.pathname.split("/");
const username = pathSegments[pathSegments.length-1]; // This will be replaced by actual username after login
setUsername(username); // Set the username when page loads
