# 🍣 Sushi Bar REST API – Lab 3

This project is a Spring Boot-based RESTful web application for managing a sushi bar system.  
It was developed as part of **Lab 3 – Information Systems Modeling** to demonstrate layered architecture and persistence using Spring Data JPA and H2.

---

## 🧱 Project Architecture

This application follows a clean **3-layer architecture**:

```
Sushi-Bar/
├── Sushi-Bar-api/          # Swagger-generated DTOs and interface stubs
└── Sushi-Bar-services/     # Main Spring Boot app (controllers, services, entities)
    ├── controllers/        # REST endpoints (implement Swagger interfaces)
    ├── services/           # Business logic layer
    ├── repositories/       # JPA data access layer
    ├── entities/           # JPA entities mapped to database tables
    ├── mappers/            # DTO ↔ Entity converters
    └── resources/
        └── application.properties
```

---

## 🔧 Technologies Used

- Java 17+
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- H2 Database (in-memory)
- Swagger / OpenAPI (springdoc-openapi)
- Maven

---

## ⚙️ How to Build the Application

From the root folder (`Sushi-Bar`), run:

```bash
./mvnw clean install
```

> On Windows, use:
> ```bash
> mvnw.cmd clean install
> ```

This will:
- Compile all modules
- Generate code from OpenAPI if needed
- Package the application

---

## ▶️ How to Run the Application

Start the Spring Boot app:

```bash
./mvnw spring-boot:run -pl Sushi-Bar-services
```

> (You can also run it directly from your IDE by starting the `SushiBarApplication.java` class)

---

## 🌐 Accessing the Application

Once running, access:

- 🔹 **Swagger UI (API Explorer):**  
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

- 🔹 **H2 Database Console (for dev testing):**  
  [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
  - JDBC URL: `jdbc:h2:mem:sushibar_db`  
  - Username: `sa`  
  - Password: *(leave blank)*

---

## 📦 Features & Endpoints

| Feature        | Endpoint Prefix | Description                        |
|----------------|------------------|------------------------------------|
| **Menu**       | `/menu`          | Add, update, delete sushi items    |
| **Users**      | `/users`         | Register, update, list users       |
| **Orders**     | `/orders`        | Place/view customer orders         |
| **Reservations** | `/reservations`| Make and manage table reservations |

All endpoints follow the REST style and are documented via Swagger UI.

---

## 💡 Example JSON for Testing

### POST /users

```json
{
  "username": "mohammed_q",
  "email": "mohammed@example.com",
  "phone": "123-456-7890",
  "role": "admin"
}
```

### POST /menu

```json
{
  "name": "Spicy Tuna Roll",
  "price": 10.99,
  "description": "Fresh tuna with spicy mayo",
  "category": "roll",
  "imageUrl": "https://example.com/sushi.jpg"
}
```

---

## 📁 How to Submit

1. Ensure the folder includes:
   - Source code only (no `target/` folders)
   - This `README.md` file
2. Compress the entire `Sushi-Bar` folder into a `.zip` archive
3. Upload via Moodle or your university system as instructed

---

## 👨‍🎓 Project Info

- **Project Name:** Sushi Bar – Lab 3  
- **Course:** Information Systems Modeling  
- **Semester:** Summer 2024/25  
- **Student:** [Your Full Name]  
- **Instructor:** [Professor’s Name if required]

---

✅ You're all set!
