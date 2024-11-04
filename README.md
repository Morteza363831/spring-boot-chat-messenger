
---

# Spring Boot Chat Messenger

This is a simple chat messenger built with Java Spring Boot, utilizing WebSocket and a message broker for real-time messaging. User authentication is handled via Keycloak, enabling users to sign up and log in. Once logged in, each user is visible to other clients and can chat with them in real-time. Each chat session is assigned a unique topic ID based on the users’ session IDs.

## Features
- **User Authentication**: Users authenticate through Keycloak.
- **Real-time Messaging**: Messages are sent over WebSocket channels with dedicated session topics.
- **Dynamic User Availability**: Once logged in, users become visible to others and can initiate chats.
- **Future Database Migration**: The app currently uses a relational database, with plans to migrate to a JSON-based database.

## Project Setup

### Prerequisites
- **Java 17**
- **Docker** (for Keycloak setup)

### Cloning the Repository

To clone the project repository, run:
```bash
git clone https://github.com/Morteza363831/spring-boot-chat-messenger.git
cd spring-boot-chat-messenger
```

### Setting Up Keycloak with Docker

1. **Run Keycloak in Docker**: Start a Keycloak container on port 8080.
   ```bash
   docker run -p 8080:8080 --name keycloak \
   -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin \
   quay.io/keycloak/keycloak:latest start-dev
   ```

2. **Import Realm Configuration**: Import the Keycloak realm configuration by going to the Keycloak admin console:
   - Access Keycloak at `http://localhost:8080`.
   - Log in with the Keycloak admin credentials (`admin/admin` by default).
   - Import your realm settings from the provided export file:
     1. Go to **Realm Settings** > **Add Realm**.
     2. Select **Import** and upload the provided `.json` file.
   - This configuration sets up the authentication required by the chat messenger app.
   - The Realm Setting file is in the `src/main/resources` directory of the project

### Running the Application

1. **Start the Application**:
   - Navigate to the project directory and run:
   ```bash
   ./mvnw spring-boot:run
   ```
   - The application will start on port `8020`. Keycloak will be available on port `8080`.

2. **Access the Application**:
   - Open your browser and navigate to `http://localhost:8020`.
   - Log in with your Keycloak credentials, and start chatting with other online users.

### Key Functionalities

- **Messaging**: Users can send and receive messages in real-time.
- **Session-based Topics**: Each chat session is assigned a unique topic based on the session ID (a combination of usernames).
- **User Presence**: Once logged in, users appear to others as available to chat.

### Future Plans
- **Database Migration**: Transition from a relational database to a JSON-based database for storing chat history and user data.

## Contact

For questions or contributions, reach out to me:

- **Email**: ali16mar15couples@gmail.com
- **LinkedIn**: [Morteza Mahdi Zadeh](https://www.linkedin.com/in/morteza-mahdi-zadeh-2a0090303)
- **Telegram**: [@m_mhzd](http://t.me/m_mhzd)

---
