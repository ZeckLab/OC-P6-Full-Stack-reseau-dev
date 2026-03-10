# 🌐 MDD — Developer Social Network (MVP)
### Student Project — OpenClassrooms

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-brightgreen)
![Angular](https://img.shields.io/badge/Angular-21-red)
![Node.js](https://img.shields.io/badge/Node.js-20.20-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Status](https://img.shields.io/badge/Project-MVP-success)

This repository contains the full‑stack MVP of **MDD (Monde de Dév)**, a fictional developer‑focused social network built as part of an OpenClassrooms training project.

The goal of this MVP is to deliver a minimal but functional version of the platform, allowing users to:

- subscribe to programming topics (JavaScript, Java, Python, Web3, etc.)
- view a personalized feed of articles based on their interests
- create articles
- post comments
- manage their account and topic subscriptions

The project includes:

- a **Spring Boot 3** backend exposing a secure REST API
- an **Angular 21** frontend using modern Angular patterns (signals, facades, standalone components)
- a **MySQL** database

The application follows a modular and maintainable structure, respecting SOLID principles and the technical constraints defined in the project brief.

---

# 📦 Project Structure

```text
/
├── back/        → Spring Boot 3 API (Java 21)
├── front/       → Angular 21 application
└── README.md    → Main documentation (this file)
```

---

## 🚀 Main Features

### 📰 Articles
- Display a chronological article feed
- View detailed article pages
- Create new articles

### 💬 Comments
- Display comments for each article
- Post new comments
- Instant local UI update

### 🏷️ Topics
- Browse available programming topics
- Subscribe / unsubscribe to topics

### 👤 Account
- View user profile
- Update profile information
- Manage topic subscriptions

### 🔐 Authentication
- Login and registration
- JWT authentication using an RSA key pair (RS256)
- Route protection with guards
- Interceptors
- Centralized error messages (back and front)


## 🛠️ Technologies

### Backend
- Java 21
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA
- MySQL

### Frontend
- Angular 21
- Standalone components
- Signals
- Facade pattern
- TypeScript

---

---

# ⚙️ Installation

## 1. Clone the repository

```bash
git clone https://github.com/ZeckLab/OC-P6-Full-Stack-reseau-dev.git
cd OC-P6-Full-Stack-reseau-dev
```

## 2. Configuration (Before Running the Backend)

Before running the backend, several configuration steps are required to ensure the application works properly.

This section covers all required configuration steps before starting the backend:
1. Database setup
2. Environment variables
3. RSA key generation

---

### 2.1 🗄️ Database Setup

Create the database:

```sql
CREATE DATABASE db_name CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Create a dedicated user:

```sql
CREATE USER 'your_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON db_name.* TO 'your_user'@'localhost';
FLUSH PRIVILEGES;
```

Replace `your_user` and `your_password` with your own credentials and update them in the `.env` file.

The database is automatically populated with initial topics via `data.sql`.

---

### 2.2 🔧 Environment Variables (Backend)

Create a `.env` file at the root of the `back/` folder.  
You can use the provided `.env.example` file as a reference:

```
back/.env
back/.env.example
```

Copy the content of `.env.example` into `.env`, then update the values with your own configuration.

#### `.env.example` (template to copy)

```env
DB_URL=jdbc:mysql://localhost:3306/db_name
DB_USER=your_user
DB_PASSWORD=your_password

JWT_PRIVATE_KEY_PATH=keys/your-private-key.der
JWT_PUBLIC_KEY_PATH=keys/your-public-key.der
JWT_EXPIRATION_MS=3600000

CORS_ALLOWED_ORIGINS=http://localhost:4200,http://example.com
```

#### ⚠️ Important
- Variable names must match the Spring configuration exactly.
- Restart the backend after modifying `.env`.
- Do not commit the `.env` file.

---

### 2.3 🔐 RSA Key Generation (DER format)

Generate the RSA key pair inside:

```
back/src/main/resources/keys
```

Commands:

```bash
cd back/src/main/resources
mkdir keys
cd keys

openssl genrsa -out private.pem 2048
openssl pkcs8 -topk8 -inform PEM -outform DER -in private.pem -out your-private-key.der -nocrypt
openssl rsa -in private.pem -pubout -outform DER -out your-public-key.der
rm private.pem
```

Update your `.env` accordingly:

```env
JWT_PRIVATE_KEY_PATH=src/main/resources/keys/your-private-key.der
JWT_PUBLIC_KEY_PATH=src/main/resources/keys/your-public-key.der
```

---

## 3. 🧩 Backend — Setup

### Requirements
- Java 21
- Maven 3+
- MySQL 8

### Install dependencies

```bash
cd back
mvn clean install
```

### Run the API

```bash
cd back
mvn spring-boot:run
```

API runs at:

```text
http://localhost:8080
```

---

## 4. 🎨 Frontend Setup

### Requirements
- Node.js 20.20+
- Angular CLI 21

### Run the application

```bash
cd front
npm install
ng serve
```

App runs at:

```text
http://localhost:4200
```

---

## 🧱 Architecture Overview

### Backend
- REST API
- controllers / services / repositories
- centralized error handling
- JWT‑based authentication

### Frontend
- facades for business logic
- signals for state management
- API services for HTTP communication
- mappers for data transformation
- guards for route protection

---

## 🏁 Conclusion

This monorepo delivers the complete MVP of the MDD platform, developed as part of an OpenClassrooms training project.
The backend provides a secure REST API, while the Angular frontend offers a smooth and reactive user experience.
