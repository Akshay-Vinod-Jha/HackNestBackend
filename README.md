# HackNest Backend 🚀

> A comprehensive Hackathon team-building, peer-rating, and portfolio management platform. 

HackNest is designed to solve the "teammate discovery" problem at hackathons. By leveraging a custom **Trust Score Engine**, heuristic-based **Recommendation Systems**, and an immutable **Achievement Ledger**, HackNest ensures that participants can find reliable, highly-skilled teammates while building a verifiable portfolio of their hackathon journey.

---

## 🏗️ Architecture & Tech Stack

- **Framework**: Spring Boot 3.x
- **Language**: Java 21
- **Database**: MongoDB (Local or Atlas Cloud)
- **Security**: Spring Security + Stateless JWT Authentication
- **Build Tool**: Maven

---

## ✨ Core Modules & Features

### 1. Authentication & Security
- Secure registration and login flows using bcrypt password hashing.
- Fully stateless JWT (JSON Web Token) authentication securing all internal API endpoints.

### 2. User Profiles & Analytics (`/api/profile`)
- Rich user profiles including skills, experiences, and portfolio links.
- **Analytics Engine**: Real-time aggregation of a user's total teams led, applications sent, invitations accepted, and hackathons participated in.

### 3. Hackathon & Team Management (`/api/hackathons`, `/api/teams`)
- Create and manage Hackathons (modes, statuses, dates, prize pools).
- Create Teams mapped to specific Hackathons with strict member limits and required roles/skills.

### 4. Engagement Engine (`/api/applications`, `/api/invitations`)
- Send, receive, and manage requests to join teams.
- State-machine driven workflows (`PENDING` -> `ACCEPTED` / `REJECTED`).

### 5. Recommendation Systems (`/api/recommendations`)
- **Teammate Matching**: Dynamically ranks candidates for a team based on role alignment, skill overlap, and Trust Scores.
- **Team Matching**: Suggests the best open teams for a user looking to participate.
- **Hackathon Discovery**: Recommends upcoming hackathons based on the user's tech stack and preferred domains.

### 6. Rating & Trust Engine (`/api/ratings`, `/api/trust`)
- **Peer Reviews**: Users rate their teammates across multiple dimensions (Reliability, Contribution, Skills) after a hackathon completes.
- **Trust Score**: A deterministic, heuristic-based algorithm that generates a global Trust Score (0-100) by combining peer ratings, application acceptance rates, and leadership history.

### 7. Achievement Ledger (`/api/achievements`)
- An immutable history of a user's hackathon journey.
- Tracks `PARTICIPATION`, `WINNER`, `TOP_10`, and `TEAM_LEADER` badges across events.

### 8. Global & College Leaderboards (`/api/leaderboard`)
- High-performance, denormalized ranking views.
- Sorts users by Trust Score, Achievements, and Participation globally or scoped to a specific college.
- Refreshes automatically via an asynchronous `@Scheduled` cron job.

### 9. Unified Dashboard (`/api/dashboard`)
- A "Single Source of Truth" API for the frontend.
- Aggregates recommended teams, pending notifications, live analytics, and trust scores into a single fast network call.

---

## 🛠️ Local Development & Setup

### 1. Prerequisites
- **Java 21** installed locally.
- **MongoDB** running locally on port `27017` OR a MongoDB Atlas cluster.
- **Maven** (Optional, the project includes a Maven wrapper `mvnw`).

### 2. Environment Configuration
Configuration is managed in `src/main/resources/application.properties`. 

By default, the application is ready for **MongoDB Atlas**. Open `application.properties` and replace the placeholder with your actual credentials:
```properties
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster-url>/hacknest?retryWrites=true&w=majority
```
*(If you want to use local MongoDB instead, change the URI to: `mongodb://localhost:27017/hacknest`)*

### 3. Running the Application
Use the included Maven wrapper to clean, compile, and run the server:

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Mac/Linux:**
```bash
./mvnw spring-boot:run
```

The API will start locally on `http://localhost:8080`.

---

## 📝 Known Architecture Notes (For Future Scale)
- **Multi-Document Transactions**: Currently, MongoDB transactions are implicitly isolated per document. Moving to a MongoDB Replica Set will enable full ACID `@Transactional` boundaries for complex invitation acceptance flows.
- **Notification Broker**: The system is designed to eventually hook into Apache Kafka or RabbitMQ to decouple synchronous application/invitation alerts into real-time WebSocket streams.
- **Caching**: The `/api/dashboard` and `/api/leaderboard` endpoints are prime candidates for a Redis caching layer as the user base scales.
