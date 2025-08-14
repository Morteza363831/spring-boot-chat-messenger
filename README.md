# Spring Boot Chat Messenger - Enterprise Microservices Architecture

## 📌 Overview

The Spring Boot Chat Messenger is a production-ready, enterprise-grade real-time chat application built using a microservices architecture with Spring Boot. It implements the Command Query Responsibility Segregation (CQRS) pattern, event-driven design, and WebSocket with STOMP protocol for instant messaging. The application emphasizes scalability, comprehensive security, and advanced search capabilities, making it suitable for enterprise-level deployments.

---

## ✨ Key Features

### 🔥 Real-time Communication
- WebSocket with STOMP protocol for seamless, instant messaging.
- Session-based chat between users with support for one-to-one communication.
- Message broadcasting via `/topic` and `/queue` destinations.
- WebSocket connection endpoint: `/chat`.

### 🏗️ Microservices Architecture
- **messenger-core** (Port: 9090): Main API and WebSocket service.
- **messenger-command** (Port: 8052): Handles write operations.
- **messenger-query** (Port: 8051): Manages read operations.
- **messenger-utilities**: Shared models and utilities across services.

### 🔐 Enterprise Security
- JWT-based authentication with custom filters.
- Role-based access control (USER, ADMIN roles).
- Method-level security using `@PreAuthorize` annotations.
- AES-GCM encryption for message content.
- CORS configuration for secure cross-origin requests.

### 📊 Event-Driven Architecture
- Apache Kafka for event streaming and synchronization.
- CQRS pattern for separation of read and write operations.
- Event sourcing to ensure consistency across services.
- Dual database support: MySQL for queries and MS SQL Server for commands.

### 🔍 Advanced Search & Analytics
- Elasticsearch integration for efficient message search.
- Real-time indexing of messages via Kafka events.
- Optimized read queries with dedicated read models.

---

## 🏗 Architecture Diagram

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   messenger-    │    │   messenger-    │    │   messenger-    │
│     core        │    │    command      │    │     query       │
│   (Port 9090)   │    │   (Port 8052)   │    │   (Port 8051)   │
│                 │    │                 │    │                 │
│ • REST APIs     │    │ • Write Ops     │    │ • Read Ops      │
│ • WebSocket     │◄──►│ • MySQL         │    │ • MySQL         │
│ • JWT Auth      │    │ • MS SQL        │    │ • Elasticsearch │
│ • H2 Database   │    │ • Kafka Consumer│    │ • Kafka Consumer│
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │  Apache Kafka   │
                    │   (Port 9092)   │
                    │                 │
                    │ • Event Stream  │
                    │ • Command/Query │
                    │ • Sync Events   │
                    └─────────────────┘
```

---

## 🛠 Technology Stack

### Backend Technologies
- **Java 17**: Latest LTS version for robust performance.
- **Spring Boot 3.3.4**: Enterprise-grade framework for microservices.
- **Spring Security**: Authentication and authorization.
- **Spring WebSocket**: Real-time communication with STOMP.
- **Spring Data JPA**: Data persistence and ORM.
- **Spring Kafka**: Event streaming and messaging.

### Databases
- **H2**: In-memory database for development and testing.
- **MySQL**: Primary database for query operations.
- **MS SQL Server**: Database for command operations.
- **Elasticsearch**: Search and analytics engine.

### Message Broker
- **Apache Kafka**: Event streaming platform.
- **Zookeeper**: Coordination service for Kafka.

### Tools & Libraries
- **MapStruct**: Simplified object mapping.
- **Lombok**: Reduction of boilerplate code.
- **JWT (jjwt)**: Token-based authentication.
- **Jackson**: JSON processing.
- **SpringDoc OpenAPI**: Interactive API documentation.
- **Docker & Docker Compose**: Containerization for deployment.
- **Maven**: Multi-module build system.
- **GitHub Actions**: CI/CD pipeline automation.

---

## 📊 Data Models

### Core Entities

#### UserEntity
```java
public class UserEntity {
    UUID id;
    String username;
    String email;
    String firstName;
    String lastName;
    String authorities;
    String password;
    boolean enabled;
}
```

#### SessionEntity
```java
public class SessionEntity {
    UUID id;
    UserEntity user1;
    UserEntity user2;
}
```

#### MessageEntity
```java
public class MessageEntity {
    UUID id;
    String content;
    UUID sessionId;
    String encryptedAesKey;
}
```

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- MySQL 8.0+
- MS SQL Server (optional for command database)
- Elasticsearch (optional for search functionality)

### 1. Clone Repository
```bash
git clone https://github.com/Morteza363831/spring-boot-chat-messenger.git
cd spring-boot-chat-messenger
```

### 2. Start Infrastructure
```bash
# Start Kafka & Zookeeper
docker-compose -f docker/kafka-compose.yml up -d

# Start MySQL
docker-compose -f docker/docker-compose.yml up -d

# Optional: Start Elasticsearch
docker-compose -f docker/elastic-compose.yml up -d
```

### 3. Build & Run Services
```bash
# Build all modules
mvn clean install

# Start messenger-core (Main API)
cd messenger-core
mvn spring-boot:run

# Start messenger-command (Write Service)
cd ../messenger-command
mvn spring-boot:run

# Start messenger-query (Read Service)
cd ../messenger-query
mvn spring-boot:run
```

### 4. Access Services
- **Main API**: `http://localhost:9090`
- **API Documentation**: `http://localhost:9090/messenger-swagger`
- **H2 Console**: `http://localhost:9090/h2-console`
- **Kafka UI**: `http://localhost:8090`

---

## 📡 API Endpoints

### Authentication
```
POST /api/v1/auth/token          # Obtain JWT token
POST /api/v1/users/register      # Register new user
```

### User Management
```
GET    /api/v1/users             # Retrieve all users
GET    /api/v1/users/{username}  # Retrieve user by username
PUT    /api/v1/users/{username}  # Update user details
DELETE /api/v1/users/delete      # Delete user
```

### Session Management
```
POST   /api/v1/sessions          # Create chat session
GET    /api/v1/sessions          # Retrieve user sessions
DELETE /api/v1/sessions/{id}     # Delete session
```

### Message Operations
```
POST   /api/v1/messages          # Send message
GET    /api/v1/messages/{sessionId} # Retrieve session messages
```

### WebSocket Connection
```javascript
// Connect to WebSocket
const socket = new SockJS('/chat');
const stompClient = Stomp.over(socket);

// Subscribe to messages
stompClient.subscribe('/topic/messages/' + sessionId, function(message) {
    // Handle incoming message
});

// Send message
stompClient.send('/app/chat/' + sessionId, {}, JSON.stringify(messageData));
```

---

## 🔧 Configuration

### Application Ports
- **messenger-core**: 9090
- **messenger-command**: 8052
- **messenger-query**: 8051
- **Kafka**: 9092
- **Kafka UI**: 8090
- **MySQL**: 3306
- **MS SQL**: 1433

### Database Configuration
```properties
# messenger-core (H2)
spring.datasource.url=jdbc:h2:./data/testdb

# messenger-command (MySQL + MS SQL)
spring.datasource.mysql.url=jdbc:mysql://localhost:3306/messengerDevDb
spring.datasource.mssql.url=jdbc:sqlserver://localhost:1433;database=messengerReadDevDb

# messenger-query (MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/messengerDevDb
```

### Kafka Configuration
```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.value-deserializer=JsonDeserializer
spring.kafka.producer.value-serializer=JsonSerializer
```

---

## 🔄 Event Flow

### Command Flow (Write Operations)
1. Client → REST API → `messenger-core`
2. `messenger-core` → Kafka Producer → Command Topic
3. `messenger-command` → Kafka Consumer → Database Write
4. `messenger-command` → Kafka Producer → Sync Topic

### Query Flow (Read Operations)
1. Client → REST API → `messenger-core`
2. `messenger-core` → Feign Client → `messenger-query`
3. `messenger-query` → Read Database → Response

### Real-time Flow (WebSocket)
1. Client → WebSocket → `messenger-core`
2. `messenger-core` → Message Broker → All Connected Clients

---

## 🔐 Security Features

### JWT Authentication
```java
@PreAuthorize("isMatch(#username) || hasAccess('ROLE_ADMIN')")
public ResponseEntity<?> updateUser(@PathVariable String username) {
    // Method implementation
}
```

### Message Encryption
- AES-GCM encryption for message content.
- Unique encryption keys per message.
- Secure key storage in the database.

### CORS Configuration
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    return source;
}
```

---

## 📈 Monitoring & Observability

- **Kafka UI**: Monitor message broker at `http://localhost:8090`.
- **H2 Console**: Inspect database at `http://localhost:9090/h2-console`.
- **Spring Boot Actuator**: Access health and metrics endpoints.
- **Swagger UI**: Test APIs at `http://localhost:9090/messenger-swagger`.

### Logging Configuration
```properties
logging.level.com.example.messenger=error
logging.level.org.springframework.kafka=info
```

---

## 🧪 Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run specific module tests
cd messenger-core && mvn test
cd messenger-command && mvn test
cd messenger-query && mvn test
```

### Integration Testing
- **TestContainers**: For database testing.
- **Kafka Test**: For event streaming validation.
- **WebSocket Test**: For real-time communication testing.

---

## 🚀 Deployment

### Docker Deployment
```bash
# Build Docker images
docker build -t messenger-core ./messenger-core
docker build -t messenger-command ./messenger-command
docker build -t messenger-query ./messenger-query

# Run with Docker Compose
docker-compose up -d
```

### Production Configuration
```properties
# Use external databases
spring.datasource.url=${DATABASE_URL}
spring.kafka.bootstrap-servers=${KAFKA_SERVERS}

# Security settings
jwt.secret=${JWT_SECRET}
encryption.key=${ENCRYPTION_KEY}
```

---

## 🔮 Future Roadmap

### Phase 1: UI Development
- Develop a React/Angular-based frontend.
- Create a mobile app using React Native.
- Implement a real-time chat interface.

### Phase 2: Enhanced Features
- Add file sharing capabilities.
- Support group chat functionality.
- Include message reactions and replies.
- Implement user presence indicators.

### Phase 3: Advanced Security
- Introduce end-to-end encryption.
- Integrate OAuth2 for authentication.
- Implement rate limiting and message retention policies.

### Phase 4: Scalability
- Transition to RabbitMQ for message brokering.
- Add Redis caching for performance optimization.
- Implement load balancing and Kubernetes deployment.

---

## 🤝 Contributing

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/amazing-feature`).
3. Commit changes (`git commit -m 'Add amazing feature'`).
4. Push to the branch (`git push origin feature/amazing-feature`).
5. Open a Pull Request.

### Development Guidelines
- Adhere to Clean Code principles.
- Write comprehensive unit and integration tests.
- Update documentation for all changes.
- Follow conventional commit messages.

---

## 👥 Contributors

Thanks to these awesome people for contributing:

| [<img src="https://github.com/morteza363831.png" width="100px" alt="Morteza Mahdi Zadeh"/>](https://github.com/morteza363831) |                                                                                                       [<img src="https://github.com/8Whoknow3.png" width="100px" alt="MohammadReza Asgari"/>](https://github.com/8Whoknow3) |                                                                                                                     [<img src="https://github.com/alisedig.png" width="100px" alt="Ali Sedighi"/>](https://github.com/alisedig) |                                                                                                                                     [<img src="https://github.com/moonwinee.png" width="100px" alt="Soheil Nouhi"/>](https://github.com/moonwinee) |                                                                                                                                     [<img src="https://github.com/anemo113.png" width="100px" alt="Ehsan Shahsavari"/>](https://github.com/anemo113) |                                                                                                                                      [<img src="https://github.com/username.png" width="100px" alt="name"/>](https://github.com/username) |                                                                                                                 
| :----------------------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------------------: |:----------------------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------------------: |
| [Morteza Mahdi Zadeh](https://github.com/morteza363831) |                                                                                                                                                                                 [MohammadReza Asgari](https://github.com/8Whoknow3) |                                                                                                                                                                                      [Ali Sedighi](https://github.com/alisedig) |                                                                                                                                                                                                  [Soheil Nouhi](https://github.com/moonwinee) |                                                                                                                                                                                                   [Ehsan Shahsavari](https://github.com/anemo113) |                                                                                                                                                                                                   [Danial khalaji](https://github.com/Danielkhj) |


---

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## 📞 Support & Contact

- **Email**: [morteza363831official@gmail.com](mailto:morteza363831official@gmail.com)
- **Telegram**: [@m_mhzd](https://t.me/m_mhzd)
- **LinkedIn**: [Morteza Mahdi Zadeh](https://www.linkedin.com/in/your-linkedin-profile)
- **Issues**: [GitHub Issues](https://github.com/Morteza363831/spring-boot-chat-messenger/issues)
- **Discussions**: [GitHub Discussions](https://github.com/Morteza363831/spring-boot-chat-messenger/discussions)

⭐ **Star this repository** if you find it helpful!

Happy Coding! 🚀
