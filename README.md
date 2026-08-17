# Electronic City Ticket System

A full-stack web application for managing electronic public transport tickets.

The system supports three user roles: **passengers**, **ticket inspectors**, and **administrators**. Passengers can browse available ticket types, purchase and activate tickets, and view their ticket history. Ticket inspectors can verify ticket validity, while administrators can manage the available ticket offering.

The application was developed as a university software engineering project and covers the complete flow from a relational database and REST API to a browser-based user interface.

## Key Features

* User registration and authentication
* Role-based authorization
* JWT-based authentication
* Ticket purchase and activation
* One-time, time-based and period tickets
* Ticket validation by inspectors
* Ticket history
* Ticket type management for administrators
* PostgreSQL database migrations and seed data with Liquibase
* REST API documentation with Swagger / OpenAPI
* Backend unit and integration tests

## Technology Stack

**Backend**

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* PostgreSQL
* Liquibase
* JWT
* Maven
* JUnit 5 / Mockito
* SpringDoc OpenAPI

**Frontend**

* Angular 19
* TypeScript
* Angular Material
* RxJS

**Development**

* Docker Compose
* PostgreSQL
* Swagger UI

## Architecture

The application follows a client-server architecture. The Angular frontend communicates with a Spring Boot REST API, while application data is stored in PostgreSQL. Authentication is handled using JWT tokens and Spring Security.

### Database Schema

<img src="/assets/bilet-miejski-database-schema.png" width="700px"/>

## Running the Project

PostgreSQL databases for development and testing can be started using:

```bash
docker compose up -d
```

Start the backend from the `backend` directory:

```bash
./mvnw spring-boot:run
```

Start the frontend from the `frontend` directory:

```bash
npm install
npm start
```

The frontend is available at:

```text
http://localhost:4200
```

Swagger API documentation is available at:

```text
http://localhost:8080/swagger-ui.html
```

## Authors

* Aleksandra Piątek
* Dominika Sachanbińska
