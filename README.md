# Job Application Tracker

A full-stack web application for tracking job applications — built as a hands-on project to demonstrate backend architecture, authentication, and full-stack integration using Spring Boot, PostgreSQL, and React.

## Features

- **Secure authentication** — user registration and login with BCrypt password hashing and stateless JWT-based sessions
- **Ownership-scoped data access** — every user sees and manages only their own applications, enforced at the repository/service layer (IDOR-protected)
- **Full CRUD** for job applications — create, list, update status, and delete, all through a live React UI
- **Layered backend architecture** — Controller → Service → Repository → DTO, with centralized exception handling
- **Automated testing** — JUnit 5 + Mockito unit tests covering business logic and security-critical ownership checks
- **Fully containerized** — one command (`docker compose up --build`) runs the entire stack: PostgreSQL, Spring Boot backend, and an Nginx-served React frontend
- **CI on every push** — separate GitHub Actions workflows for backend (build + test) and frontend (lint + build)

## Tech Stack

| Layer | Technologies |
|---|---|
| Backend | Java, Spring Boot, Spring Security, Spring Data JPA, Hibernate |
| Auth | JWT (jjwt), BCrypt |
| Database | PostgreSQL |
| Frontend | React (Vite), React Router, Axios |
| Testing | JUnit 5, Mockito |
| Infrastructure | Docker, Docker Compose, Nginx |
| CI/CD | GitHub Actions |

## Architecture

```
┌─────────────────┐        ┌──────────────────┐        ┌──────────────┐
│  React (Nginx)   │ ─────▶ │  Spring Boot API  │ ─────▶ │  PostgreSQL  │
│  localhost:5173  │  REST  │  localhost:8080   │  JPA   │  :5432       │
└─────────────────┘        └──────────────────┘        └──────────────┘
```

**Backend request flow:** every request passes through a JWT authentication filter, which validates the token and populates Spring Security's context before reaching the controller. Controllers stay thin, delegating all business logic — including deriving the current user from the authenticated token — to the service layer. DTOs shape every API response, keeping internal entity fields (like password hashes or JPA relationships) from ever leaking to the client.

## Getting Started

### Prerequisites
- Docker Desktop

### Run the full stack

```bash
git clone https://github.com/saikiran2508/job-application-tracker.git
cd job-application-tracker
docker compose up --build
```

Then open:
- Frontend: [http://localhost:5173](http://localhost:5173)
- Backend API: [http://localhost:8080/api](http://localhost:8080/api)

### Run backend tests

```bash
cd jobtracker
mvn clean verify
```

## API Reference

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/users/register` | Register a new user | No |
| POST | `/api/users/login` | Log in, returns a JWT | No |
| GET | `/api/users/me` | Get the current user's profile | Yes |
| GET | `/api/applications` | List the current user's applications | Yes |
| POST | `/api/applications` | Create a new application | Yes |
| GET | `/api/applications/{id}` | Get one application by ID | Yes |
| PUT | `/api/applications/{id}/status` | Update an application's status | Yes |
| DELETE | `/api/applications/{id}` | Delete an application | Yes |

Protected endpoints require an `Authorization: Bearer <token>` header, obtained from the login endpoint.

## Project Structure

```
job-application-tracker/
├── jobtracker/              # Spring Boot backend
│   ├── src/main/java/...    # model, repository, service, controller, dto, security, config
│   └── src/test/java/...    # JUnit/Mockito test suite
├── jobtracker-frontend/     # React (Vite) frontend
│   └── src/
│       ├── api/             # centralized axios calls to the backend
│       ├── pages/           # Login, Register, Dashboard, Add Application
│       └── components/      # ProtectedRoute, etc.
├── docker-compose.yml       # orchestrates all three services
└── .github/workflows/       # backend and frontend CI pipelines
```

## Roadmap / Possible Enhancements

- Refresh tokens (current JWT expires after 1 hour)
- Status change history/timeline
- Public deployment (Railway/Render)

## Author

**Sai Kiran Gopu**
[LinkedIn](https://www.linkedin.com/in/saikirangopu/) · [GitHub](https://github.com/saikiran2508)
