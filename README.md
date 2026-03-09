# Identity Reconciliation Service

A Spring Boot API that **identifies and links customer contacts** based on email and phone number.
If multiple records belong to the same person, the service consolidates them under a **single primary contact**.

This project implements the **Identity Reconciliation assignment**.

---

## 🌐 Live Service

Base URL

```
https://identity-reconciliation-service-1-18g1.onrender.com
```

---

## 📚 API Documentation (Swagger)

Interactive Swagger UI:

```
https://identity-reconciliation-service-1-18g1.onrender.com/swagger-ui/index.html
```

Swagger allows you to test the API directly from the browser.

---

# 🧩 Problem Overview

Users may sign up using different emails or phone numbers over time.
The system must detect whether these records belong to the **same person**.

Example:

| Email                           | Phone |
| ------------------------------- | ----- |
| [a@test.com](mailto:a@test.com) | 111   |
| [b@test.com](mailto:b@test.com) | 111   |

Both should be linked to the **same identity**.

The service consolidates these contacts and returns a unified response.

---

# 📡 API Endpoint

### Identify Contact

**POST**

```
/identify
```

Full URL

```
https://identity-reconciliation-service-1-18g1.onrender.com/identify
```

---

# 📥 Request Example

```json
{
  "email": "test@example.com",
  "phoneNumber": "1234567890"
}
```

At least **one field must be provided**.

---

# 📤 Response Example

```json
{
  "contact": {
    "primaryContactId": 1,
    "emails": [
      "test@example.com"
    ],
    "phoneNumbers": [
      "1234567890"
    ],
    "secondaryContactIds": []
  }
}
```

---

# 🧠 Identity Resolution Logic

The system follows these steps:

1. Search existing contacts using **email or phone number**
2. If no match exists
   → create a **new primary contact**
3. If matching contacts exist
   → link them together
4. The **oldest contact becomes the primary**
5. All other contacts become **secondary**
6. Return all related emails and phone numbers

---

# 🗄️ Database Schema

### Contact Table

| Field          | Description                |
| -------------- | -------------------------- |
| id             | Unique identifier          |
| email          | Contact email              |
| phoneNumber    | Contact phone number       |
| linkedId       | References primary contact |
| linkPrecedence | PRIMARY or SECONDARY       |
| createdAt      | Creation timestamp         |
| updatedAt      | Last update timestamp      |
| deletedAt      | Soft delete timestamp      |

---

# ⚙️ Tech Stack

* **Java 21**
* **Spring Boot**
* **Spring Data JPA**
* **PostgreSQL**
* **Gradle**
* **Docker**
* **Render (Deployment)**
* **Swagger / OpenAPI**

---

# 🚀 Running Locally

Clone the repository

```
git clone https://github.com/Harshjha002/identity-reconciliation-service
```

Navigate into the project

```
cd identity-reconciliation-service
```

Build the project

```
./gradlew build
```

Run the application

```
./gradlew bootRun
```

Server will start at

```
http://localhost:8080
```

Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

# 🐳 Docker

Build the Docker image

```
docker build -t identity-service .
```

Run the container

```
docker run -p 8080:8080 identity-service
```

---

# ☁️ Deployment

The application is deployed on **Render** using Docker.

Service URL

```
https://identity-reconciliation-service-1-18g1.onrender.com
```

Swagger UI

```
https://identity-reconciliation-service-1-18g1.onrender.com/swagger-ui/index.html
```

---

# 👨‍💻 Author

**Harsh Jha**

GitHub
https://github.com/Harshjha002
