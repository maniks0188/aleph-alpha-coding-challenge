
# 🛒 Shopping List Manager (Spring Boot Backend)

A Java 17+ Spring Boot 3.x REST API for managing shopping lists and items, including a basic recommendation engine.  
It supports authentication using JWT, monitoring with Micrometer + Prometheus, and is containerized via Docker.

---

## ✅ Features

- 📦 Add, update, retrieve grocery **items**
- 🧾 Create and manage **shopping lists**
- 🔍 Autocomplete item names using prefix
- 🧠 Recommend complementary items based on shopping list
- 🔐 **JWT Authentication**
- 📘 Swagger/OpenAPI documentation
- 🧪 Unit & integration test coverage
- 📈 Micrometer + Prometheus monitoring
- 🐳 Dockerized and Kubernetes-ready

---

## ⚙️ Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- H2 Database (in-memory) / PostgreSQL
- Micrometer + Prometheus
- Swagger UI
- Docker

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker (optional for containerized run)

---

## 🔧 Run Locally

###bash
git clone https://github.com/YOUR_USERNAME/shopping-list-manager.git
cd shopping-list-manager
./mvnw clean spring-boot:run

---
##🔐 JWT Authentication
- POST /api/v1/auth/login
{
  "username": "admin"
}

- Use the returned token in headers:
Authorization: Bearer <your-token>

##🧪 Running Tests
- ./mvnw test

### **2. Run the Application Using Docker**

#### **2.1. Build the Docker Image**
Use the provided `Dockerfile` to build a Docker image:
```bash
docker build -t shopping-list-app:latest 
```
#### **2.2. Run the Docker Container**
Start the application container:
```bash
docker run -p 8080:8080 shopping-list-app
```

#### **2.3. Access the Application**
Once the container is running, the API will be accessible at:
```
http://localhost:8080
```
---

## 🌐 API Access
- Swagger UI         | [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)                 
- H2 Console         | [http://localhost:8080/h2-console](http://localhost:8080/h2-console)                   
- Prometheus Metrics | [http://localhost:8080/actuator/prometheus](http://localhost:8080/actuator/prometheus)

---

## **Troubleshooting**

### **Docker Issues**
- If the container doesn’t start, check the logs:
  - bash >>	docker logs shopping-list-app
  
---

## **License**
This project is licensed under the Local License. See the LICENSE file for details.
