
# Sentinel

A sophisticated backend system built with Spring Boot 4.0 that provides a secure, automated environment for personal journaling. Beyond standard CRUD operations, this application features a distributed architecture using Kafka for messaging and Redis for caching, alongside automated sentiment analysis and third-party API integrations.


## Tech Stack

**Framework:**	Spring Boot 4.0.0

**Security:**	Spring Security with JWT & BCrypt Password Encoding

**Database:**	MongoDB with @DBRef and Repository Implementations

**Messaging:**	Apache Kafka (Producer/Consumer model)

**Caching:**	Redis (String Serialization)

**Scheduling:**	Spring @Scheduled for background tasks

**Documentation:**	Swagger/OpenAPI 3.0
## Security Configuration

The application employs a robust security model defined in SpringSecurity.java:

**Public Access:** Endpoints under /public/** and Swagger UI documentation are accessible without authentication.

**Role-Based Access Control (RBAC):**
/journal/** and /user/** require a valid authenticated user.

**/admin/**** is strictly restricted to users with the ADMIN role.

**JWT Integration:** A custom JwtFilter processes tokens before the UsernamePasswordAuthenticationFilter.

**Password Safety:** All passwords are encrypted using BCryptPasswordEncoder.
## Automated Workflows & Scheduling

The **UserScheduler** component manages critical background operations:

**Sentiment Analysis Pipeline:** Periodically fetches users, analyzes the frequency of sentiments (e.g., Happy, Sad) from the last 7 days of journal entries, and publishes the result to the journalApp Kafka topic.

**Cache Management:** Automatically clears the application-level cache (Appcache) every 10 minutes using a cron expression (0 */10 * * * *).

**Fault Tolerance:** If Kafka messaging fails during the sentiment analysis process, the system automatically falls back to sending a direct email via EmailService.
## API Documentation

The project is configured with a custom OpenAPI bean that provides a professional UI for testing endpoints.

**Base URL:** http://localhost:8080

**Security Schema:** Supports bearerAuth (JWT) directly in the Swagger UI.

**Tagging:** Endpoints are logically grouped into Public, User, Journal, and Admin categories.
## Configuration & Setup

**Redis Setup:** The app expects a Redis connection factory to be available for RedisTemplate<String, String> operations.

**Kafka Setup:** Ensure a Kafka broker is running; the producer is configured to send SentimentData objects to the journalApp topic.
