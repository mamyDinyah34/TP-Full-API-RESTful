# 🚀 User Management API with Auth (JWT) + API Key

This API allows user management with secure authentication using JWT tokens and API keys. It provides endpoints for user registration, authentication, and CRUD operations on user data.

## 📌 Installation

### ✅ Prerequisites
- Java 20
- Maven
- PostgreSQL (or any other configured database)

### 📥 Steps
1. Clone the repository:
   ```sh
   git clone https://github.com/mamyDinyah34/TP-Full-API-RESTful.git
   cd TP-Full-API-RESTful
   ```
2. Configure the database in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/userapi
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
3. Install dependencies and build the project:
   ```sh
   mvn clean install
   ```
4. Run the application:
   ```sh
   mvn spring-boot:run
   ```

## 📡 Routes API 
- **POST /api/login** : CUser login (Returns a JWT token, ApiKey)
- **POST /api/register** : Register a new user
- **GET /api/users** : List all users
- **PUT /api/users/{id}** : Update a user
- **DELETE /api/users/{id}** : Delete a user

## 📖 API Documentation
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html#)
- **Postman Collection**: [Postman Documentation](https://documenter.getpostman.com/view/32711214/2sAYkBrgQQ)

---

© Mamy Dinyah - 2025
