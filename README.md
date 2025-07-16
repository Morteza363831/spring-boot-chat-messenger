# Chat Messenger - Spring Boot WebSocket & REST API

## 📌 Overview
This is a **Spring Boot** based **real-time chat messenger** that utilizes **WebSocket** for instant messaging and **RESTful APIs** for user, session, and message management. The project follows a clean-code approach with structured exception handling, authentication, and API documentation.

## ✨ Key Features
- **Real-time Communication:** Uses **WebSocket** for real-time messaging.
- **Message Broker:** Currently uses Spring WebSocket **default broker** (planned upgrade to **RabbitMQ**).
- **RESTful APIs:** Provides structured endpoints for **user, session, and message management**.
- **JWT Authentication:** Secures APIs with **JSON Web Tokens** (JWT).
- **Structured Response Format:** Ensures a consistent API response structure.
- **Exception Handling:** Custom exception handling for better debugging.
- **API Documentation:** Integrated with **SpringDoc Swagger** for API documentation with examples.
- **Clean Code & Simple Design:** Well-commented and easy-to-understand code.

## 🏗 Architecture
- **User Management:** Properly structured user handling.
- **Session-based Communication:** Each chat (between two users) operates using a **session ID**.
- **Efficient Message Storage:** Messages are stored as **JSON objects** instead of individual database records.
- **Database Design:** `MessageEntity` has a **one-to-one** relation with `SessionEntity`.

## 🚀 Future Improvements
- **Frontend UI:** Currently, this is a **REST application** with no UI; a front-end will be added.
- **Database Security:** Messages stored in JSON **will be encrypted**.
- **Security Enhancements:** Authentication and authorization mechanisms need further strengthening.

## 🛠 Setup & Installation

### 🔹 Prerequisites
- **Java 17**
- **Maven**
- **PostgreSQL/MySQL** (Configured in `application.properties`)
- **Spring Boot 3.x**

### 🔹 Running Locally
1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/chat-messenger.git
   cd chat-messenger
   ```
2. Configure database settings in `src/main/resources/application.properties`.
3. Build and run the application:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```
4. Access API documentation at: `http://localhost:9090/messenger-swagger`

### 🔹 Running on GitHub Codespaces
1. Enable **GitHub Codespaces** on your repository.
2. Open Codespaces and run the application using:
   ```sh
   mvn spring-boot:run
   ```
3. Expose ports if necessary to access APIs externally.

## 🗨️ Contact & Discussion
💬 **Discussion Forum:** [GitHub Discussions](https://github.com/your-username/chat-messenger/discussions)<br/>
📧 **Email:** morteza363831official@gmail.com <br/>
📱 **Telegram:** [Morteze Mahdi zadeh's Telegram](http://t.me/m_mhzd) <br/>
📱 **LinkedIn:** [Morteze Mahdi zadeh's LinkedIn](https://www.linkedin.com/in/morteza-mahdi-zadeh)


---


## 👥 Contributors

Thanks to these awesome people for contributing:

| [<img src="https://github.com/morteza363831.png" width="100px" alt="Morteza Mahdi Zadeh"/>](https://github.com/morteza363831) |                                                                                                       [<img src="https://github.com/8Whoknow3.png" width="100px" alt="MohammadReza Asgari"/>](https://github.com/8Whoknow3) |                                                                                                                     [<img src="https://github.com/username.png" width="100px" alt="name"/>](https://github.com/username) |                                                                                                                                     [<img src="https://github.com/username.png" width="100px" alt="name"/>](https://github.com/username) |                                                                                                                                     [<img src="https://github.com/username.png" width="100px" alt="name"/>](https://github.com/username) |                                                                                                                                      [<img src="https://github.com/username.png" width="100px" alt="name"/>](https://github.com/username) |                                                                                                                 
| :----------------------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------------------: |:----------------------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------------------: |
| [Morteza Mahdi Zadeh](https://github.com/morteza363831) |                                                                                                                                                                                 [MohammadReza Asgari](https://github.com/8Whoknow3) |                                                                                                                                                                                      [name](https://github.com/username) |                                                                                                                                                                                                  [name](https://github.com/username) |                                                                                                                                                                                                   [name](https://github.com/username) |                                                                                                                                                                                                   [name](https://github.com/username) |


---

## 📝 Contributing
Feel free to **fork**, **star**, and **contribute** to this project! Follow best practices and submit PRs with clear documentation.

Happy Coding! 🚀

