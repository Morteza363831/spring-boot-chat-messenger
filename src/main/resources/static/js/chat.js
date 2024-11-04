const urlParams = new URLSearchParams(window.location.search);
const sender = urlParams.get('sender'); // Get sender username
const receiver = urlParams.get('receiver'); // Get receiver username
setUsernames(sender,receiver);



function setUsernames(sender , receiver) {
    document.getElementById('sender').innerText = sender; // Set sender's name
    document.getElementById('receiver').innerText = receiver; // Set recipient's name
}

// Fetch chat history when the page loads
fetch(`/messages?sender=${sender}&receiver=${receiver}`)
    .then(response => response.json())
    .then(messages => {
        messages.forEach(msg => {
            displayMessage(msg); // Display each message
        });
        scrollToBottom(); // Scroll to bottom after loading messages
    })
    .catch(error => console.error('Error fetching messages:', error));

// Connect to WebSocket server
const socket = new SockJS('/chat'); // Connect to WebSocket endpoint
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected:', frame);

    // Subscribe to the topic for receiving messages based on chat ID or user combination
    stompClient.subscribe('/topic/' + chatId, function(message) {
        console.log("asfsdfkl;")
        const chatMessage = JSON.parse(message.body);
        displayMessage(chatMessage); // Display new incoming message
        console.log("sended message");
        document.getElementById('notificationSound').play(); // Play notification sound
        scrollToBottom(); // Scroll to bottom when new message arrives
        hideTypingIndicator(); // Hide typing indicator if visible
    });
});

function scrollToBottom() {
    const messageListDiv = document.getElementById('messageList');
    messageListDiv.scrollTop = messageListDiv.scrollHeight;
}

function showTypingIndicator() {
    const typingIndicator = document.getElementById('typingIndicator');
    typingIndicator.style.display = 'block'; // Show typing indicator
}

function hideTypingIndicator() {
    const typingIndicator = document.getElementById('typingIndicator');
    typingIndicator.style.display = 'none'; // Hide typing indicator
}

// Function to display received messages
function displayMessage(chatMessage) {
    const messageListDiv = document.getElementById('messageList');

    const li = document.createElement('li');

    const messageBox = document.createElement('div');

    // Create username box
    const usernameBox = document.createElement('div');

    const usernameSpan = document.createElement('span');

    usernameSpan.innerText = `${chatMessage.sender}`;

    // Add specific classes based on sender
    if (chatMessage.sender === sender) {
        usernameBox.classList.add('username-box', 'sender-username');
        messageBox.classList.add('message-content-box', 'sender-message');
    } else {
        usernameBox.classList.add('username-box', 'receiver-username');
        messageBox.classList.add('message-content-box', 'receiver-message');
    }

    usernameBox.appendChild(usernameSpan);
    li.appendChild(usernameBox);

    // Create message content box
    const contentSpan = document.createElement('span');
    contentSpan.innerText = chatMessage.content;

    messageBox.appendChild(contentSpan);
    li.appendChild(messageBox);

    messageListDiv.appendChild(li); // Append message to the list

    li.style.opacity = '0'; // Start with hidden opacity
    setTimeout(() => { li.style.opacity = '1'; },100); // Fade in effect

    scrollToBottom(); // Scroll to bottom when new message arrives
}

// Handle form submission
document.getElementById('messageForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent default form submission

    const messageContent = document.getElementById('message').value.trim(); // Get trimmed message content
    console.log("tt")
    if (messageContent) { // Only send if there's content
        const chatMessage = { sender, receiver, content : messageContent }; // Create the message object

        stompClient.send("/app/send", {}, JSON.stringify(chatMessage)); // Send message to server
        displayMessage(chatMessage); // Display new incoming message
        document.getElementById('message').value = ''; // Clear input after sending

        stompClient.send("/app/typing", {}, JSON.stringify({ sender })); // Notify others that user is typing (for demonstration)

        hideTypingIndicator(); // Hide typing indicator when sending a message
    }
});