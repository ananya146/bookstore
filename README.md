# Bookstore — Full-Stack E-Commerce Application

A full-stack bookstore web application that allows users to browse books, explore recommendations, manage their shopping cart, place orders, and track their purchase history.

Built using **Angular, Spring Boot, and PostgreSQL**, the project demonstrates frontend-backend integration, REST API development, database management, and e-commerce workflows.

## ✨ Features

- **User Authentication:** Register, log in, and log out.
- **Book Catalog:** Browse available books and apply filters.
- **Book Recommendations:** Discover books recommended by the application.
- **Shopping Cart:** Add and remove books and update quantities.
- **Checkout:** Add a new delivery address or select a saved address.
- **Payment Flow:** Select an available payment method and complete checkout.
- **Loyalty Points:** Use eligible points earned through previous purchases.
- **Order History:** View previous purchases and order details.
- **Order Cancellation:** Cancel eligible orders.
- **Buy Again:** Add previously purchased books back to the cart.

  
![Uploading image.png…]()

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Angular | Frontend framework |
| TypeScript | Frontend application logic |
| HTML5 | Page structure |
| CSS3 | Styling and layout |
| Java | Backend programming language |
| Spring Boot | Backend application and REST APIs |
| Spring Data JPA | Database access and persistence |
| PostgreSQL | Relational database |
| Maven | Backend dependency and build management |
| Git & GitHub | Version control and source code hosting |

## 🏗️ Application Architecture

The application follows a frontend-backend architecture with a layered backend.

```text
Bookstore
│
├── Frontend (Angular)
│   ├── Components
│   ├── Services
│   └── App Routes
│
├── Backend (Spring Boot)
│   ├── Controllers
│   ├── Services
│   ├── Repositories
│   └── Entities
│
└── Database (PostgreSQL)
```

### Frontend

- **Components:** Manage the user interface and user interactions.
- **Services:** Handle reusable functionality and HTTP communication with the backend.
- **App Routes:** Configure navigation between application pages.

### Backend

- **Controllers:** Expose REST endpoints and handle incoming HTTP requests.
- **Services:** Implement business logic and coordinate application operations.
- **Repositories:** Perform database operations using Spring Data JPA.
- **Entities:** Represent application data mapped to database tables.

### Database

PostgreSQL stores persistent application data, including records for users, books, and orders, according to the implemented data model.

## ⚙️ Getting Started

### Prerequisites

Install the following before running the application:

- Node.js and npm
- Angular CLI
- Java Development Kit (JDK)
- Maven
- PostgreSQL

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/bookstore.git
cd bookstore
```

### 2. Configure the Database

Create a PostgreSQL database for the application.

```sql
CREATE DATABASE bookstore_db;
```

Configure the backend database connection using your local PostgreSQL credentials. Keep passwords and other secrets in local configuration or environment variables; do not commit them to GitHub.

### 3. Run the Backend

Open a terminal in the backend directory:

```bash
cd backend
mvn spring-boot:run
```

The Spring Boot application typically runs on `http://localhost:8080`, unless configured otherwise.

### 4. Run the Frontend

Open another terminal in the frontend directory:

```bash
cd frontend
npm install
ng serve
```

Open `http://localhost:4200` in your browser.

> **Note:** The commands above assume the frontend directory is named `frontend`. Adjust the path and database configuration to match the actual project structure. Any additional application-specific configuration or database initialization must also be completed before running the application.

## 🔄 Application Workflow

1. Register a new account or log in.
2. Browse books and explore recommendations.
3. Filter the catalog and add books to the cart.
4. Manage quantities and review the cart.
5. Select or add a delivery address.
6. Choose a payment method and complete checkout.
7. Review order confirmation and loyalty points.
8. View order history, cancel eligible orders, or buy previously purchased books again.

## 🎯 Project Objectives

- Build a full-stack web application using Angular and Spring Boot.
- Implement REST API communication between frontend and backend.
- Organize backend logic using a layered architecture.
- Integrate a relational database using Spring Data JPA.
- Implement common e-commerce workflows.
- Practice application development, database integration, and version control.

---

*This project is intended for learning and demonstration purposes. Features such as payment processing, authentication security, and deployment depend on their actual implementation and configuration.*
