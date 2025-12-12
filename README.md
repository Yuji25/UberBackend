# 🚗 Uber Backend - Ride Sharing System

A production-grade Spring Boot backend application for a ride-sharing system (like Uber) with JWT authentication, MongoDB integration, and advanced analytics.

## 🌟 Features

### Core Functionality
- ✅ **User Authentication** - JWT-based authentication with role support
- ✅ **Role-Based Access Control** - Drivers and Passengers with different permissions
- ✅ **Ride Management** - Create, Accept, and Complete rides
- ✅ **Transaction Support** - @Transactional operations for data consistency
- ✅ **Advanced Queries** - Complex MongoDB queries with filtering, sorting, and pagination
- ✅ **Analytics Dashboard** - MongoDB aggregation pipelines for business insights
- ✅ **Interactive API Documentation** - Swagger/OpenAPI with UI for testing endpoints

### Security
- ✅ JWT token-based authentication (stateless)
- ✅ BCrypt password encryption
- ✅ Role-based authorization (ROLE_USER, ROLE_DRIVER)
- ✅ Ownership validation for ride operations
- ✅ Global exception handling

### APIs (23 REST Endpoints)
- **Authentication** (2): Register, Login
- **Health Checks** (2): Ping, Database connectivity
- **Core Rides** (4): Create, Accept, Complete, Get All
- **Advanced Queries** (10): Search, Filter, Sort, Pagination
- **Analytics** (5): Earnings, Summaries, Reports

---

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Database**: MongoDB (NoSQL)
- **Security**: Spring Security + JWT
- **Build Tool**: Maven
- **ORM**: Spring Data MongoDB
- **API Documentation**: Swagger/OpenAPI 3.0 (springdoc-openapi)

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

1. **Java 17 or higher**
   - Download: https://www.oracle.com/java/technologies/downloads/
   - Verify: `java -version`

2. **Maven 3.6+**
   - Usually comes with IntelliJ IDEA
   - Verify: `mvn -version`

3. **MongoDB Atlas Account** (Free tier available)
   - Sign up: https://www.mongodb.com/cloud/atlas
   - OR install MongoDB locally: https://www.mongodb.com/try/download/community

4. **IDE** (Recommended)
   - IntelliJ IDEA Ultimate
   - OR Eclipse with Spring Tools
   - OR VS Code with Java extensions

---

## 🚀 Quick Start Guide

### Step 1: Clone the Repository

```bash
git clone https://github.com/Yuji25/UberBackend.git
cd UberBackend
```

### Step 2: Set Up MongoDB

#### Option A: MongoDB Atlas (Cloud - Recommended)
1. Create account at https://www.mongodb.com/cloud/atlas
2. Create a new cluster (free M0 tier available)
3. Create a database user:
   - Go to "Database Access"
   - Add new user with username and password
   - Remember these credentials!
4. Whitelist your IP:
   - Go to "Network Access"
   - Add IP Address (0.0.0.0/0 for development)
5. Get connection string:
   - Click "Connect" on your cluster
   - Choose "Connect your application"
   - Copy the connection string

#### Option B: Local MongoDB
1. Install MongoDB Community Edition
2. Start MongoDB service
3. Connection string: `mongodb://localhost:27017/uberdb`

### Step 3: Configure Application

1. **Copy the example configuration file:**
   ```bash
   cd src/main/resources
   cp application.properties.example application.properties
   ```

2. **Edit `application.properties`** with your actual values:
   ```properties
   # Replace with your MongoDB connection string
   spring.data.mongodb.uri=mongodb+srv://YOUR_USERNAME:YOUR_PASSWORD@YOUR_CLUSTER.mongodb.net/uberdb
   
   # Generate a strong JWT secret (minimum 32 characters)
   jwt.secret=YOUR_STRONG_SECRET_KEY_HERE_AT_LEAST_32_CHARACTERS
   ```

   **Important**: 
   - Replace `YOUR_USERNAME` and `YOUR_PASSWORD` with your MongoDB credentials
   - Replace `YOUR_CLUSTER` with your cluster URL
   - Generate a secure JWT secret (you can use: https://www.allkeysgenerator.com/)

### Step 4: Build and Run

#### Using IntelliJ IDEA:
1. Open the project in IntelliJ IDEA
2. Wait for Maven to download dependencies
3. Run `UberBackendApplication.java`
4. Application starts on `http://localhost:8081`

#### Using Command Line:
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Step 5: Verify Setup

**Test 1: Health Check**
```bash
curl http://localhost:8081/api/health/ping
```
Expected response: `{"status":"success","message":"Application is running!"}`

**Test 2: Database Check**
```bash
curl http://localhost:8081/api/health/db
```
Expected response: `{"status":"success","message":"Database connection is healthy!"}`

---

## 📖 Interactive API Documentation (Swagger UI)

### 🎉 Explore & Test APIs with Swagger!

This project includes **interactive API documentation** powered by Swagger/OpenAPI 3.0. You can view all endpoints, see request/response examples, and **test APIs directly from your browser**!

### 🚀 Access Swagger UI

Once the application is running, open your browser and navigate to:

```
http://localhost:8081/swagger-ui.html
```

### ✨ What You'll See

**Swagger UI Features:**
- 📋 **Complete API List** - All 23 REST endpoints in one place
- 📂 **Organized Groups** - APIs grouped by functionality (Authentication, Rides, Analytics, etc.)
- 🧪 **Try It Out** - Test any endpoint directly from the browser
- 🔒 **JWT Authorization** - Built-in authorization with "Authorize" button
- 📝 **Request/Response Examples** - See exactly what to send and expect
- 📊 **Response Codes** - All possible responses (200, 403, 500, etc.) with examples
- 🎯 **Parameter Descriptions** - Detailed info for every parameter

### 🎯 How to Use Swagger UI

#### Step 1: Register a User
1. Open Swagger UI: `http://localhost:8081/swagger-ui.html`
2. Find **"Authentication APIs"** section
3. Click on `POST /api/auth/register`
4. Click **"Try it out"** button
5. Enter request body:
   ```json
   {
     "username": "testuser",
     "password": "password123",
     "role": "ROLE_USER"
   }
   ```
6. Click **"Execute"**
7. See the response with auto-generated ID!

#### Step 2: Login & Get JWT Token
1. Click on `POST /api/auth/login`
2. Click **"Try it out"**
3. Enter credentials:
   ```json
   {
     "username": "testuser",
     "password": "password123"
   }
   ```
4. Click **"Execute"**
5. **Copy the JWT token** from the response (the long string starting with `eyJ...`)

#### Step 3: Authorize for Protected Endpoints
1. Click the **"Authorize" 🔓** button at the top right of Swagger UI
2. In the popup, enter: `Bearer <paste-your-token-here>`
   - Example: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciI...`
   - **Important:** Include the word "Bearer" followed by a space, then your token
3. Click **"Authorize"**
4. Click **"Close"**
5. The lock icon 🔓 should now be closed 🔒 (you're authorized!)

#### Step 4: Test Protected Endpoints
1. Find **"Core Ride APIs"** section
2. Click on `POST /api/rides`
3. Click **"Try it out"**
4. Enter ride details:
   ```json
   {
     "pickupLocation": "Downtown Station",
     "dropLocation": "Airport Terminal 2",
     "fare": 25.50,
     "distanceKm": 15.3
   }
   ```
5. Click **"Execute"**
6. ✅ Success! You just created a ride using Swagger!

### 📂 API Groups Available

The APIs are organized into **5 logical groups** for easy navigation:

| Group | Description | Endpoints |
|-------|-------------|-----------|
| 🔐 **Authentication APIs** | User registration and login | 2 endpoints |
| 🚗 **Core Ride APIs** | Create, accept, complete rides + queries | 14 endpoints |
| 🔍 **Advanced Query APIs** | Complex searches with filters & pagination | Same as above (organized view) |
| 📊 **Analytics APIs** | Business analytics & aggregations | 5 endpoints |
| 💚 **Health Check APIs** | Application & database health | 2 endpoints |

**Total: 23 REST Endpoints** - All documented and testable!

### 🔑 Key Features of Our Swagger Implementation

#### 1. **Smart Request Body Handling**
- ✅ Auto-generated fields (like `id`, `status`, `timestamps`) are **hidden** from request bodies
- ✅ Only required fields are shown when creating resources
- ✅ Clear examples for every field

#### 2. **Accurate Response Examples**
- ✅ **Success responses (200)** show the actual object (User, Ride, etc.)
- ✅ **Error responses (403, 500)** show proper error structure with:
  - `timestamp` - When the error occurred
  - `status` - HTTP status code
  - `error` - Error type
  - `message` - Detailed error message

#### 3. **JWT Security Integration**
- ✅ **"Authorize" button** in UI for easy token management
- ✅ Enter token once, test all protected endpoints
- ✅ Clear indication of which endpoints require authentication
- ✅ Proper 403 responses for unauthorized access

#### 4. **Detailed Descriptions**
Every endpoint includes:
- 📝 Summary - Quick overview
- 📖 Description - Detailed explanation
- 🎯 Parameters - What each parameter does
- 📊 Response Codes - All possible outcomes
- 💡 Examples - Sample requests and responses

### 🌐 Alternative Swagger URLs

- **Main Swagger UI:** `http://localhost:8081/swagger-ui.html`
- **OpenAPI JSON (All APIs):** `http://localhost:8081/api-docs`
- **OpenAPI JSON (By Group):**
  - Authentication: `http://localhost:8081/api-docs/auth`
  - Core Rides: `http://localhost:8081/api-docs/core-rides`
  - Queries: `http://localhost:8081/api-docs/queries`
  - Analytics: `http://localhost:8081/api-docs/analytics`
  - Health: `http://localhost:8081/api-docs/health`

### 💡 Pro Tips

1. **Use the "Authorize" button** - Don't manually add Bearer token to each request
2. **Check response schemas** - Click "Schema" tab to see field types and requirements
3. **Copy curl commands** - Each request has a curl equivalent you can use in terminal
4. **Test error cases** - Try invalid data to see error responses
5. **Export API specification** - Download OpenAPI JSON for API client generation

### 🎨 Swagger UI Screenshot Features

When you open Swagger UI, you'll see:

```
┌─────────────────────────────────────────────────────────┐
│  UberBackend API Documentation                v1.0.0    │
│  Complete REST API for ride-sharing system             │
│                                         [Authorize 🔓]  │
├─────────────────────────────────────────────────────────┤
│  Select a definition: [default ▼]                      │
│  ┌─ Authentication APIs ──────────────────────────┐    │
│  │  POST /api/auth/register  Register new user    │    │
│  │  POST /api/auth/login     Login user          │    │
│  └────────────────────────────────────────────────┘    │
│  ┌─ Core Ride APIs ───────────────────────────────┐    │
│  │  POST /api/rides          Create new ride      │    │
│  │  POST /api/rides/accept   Accept ride          │    │
│  │  POST /api/rides/complete Complete ride        │    │
│  │  GET  /api/rides          Get all rides        │    │
│  │  ... and 10 more query endpoints               │    │
│  └────────────────────────────────────────────────┘    │
│  ┌─ Analytics APIs ───────────────────────────────┐    │
│  │  GET  /api/analytics/driver/{id}/earnings      │    │
│  │  GET  /api/analytics/rides-per-day             │    │
│  │  ... and 3 more analytics endpoints            │    │
│  └────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
```

### 🎓 Why Swagger/OpenAPI?

**For Developers:**
- 🚀 Faster development - No need for separate API testing tools
- 📝 Always up-to-date - Documentation generated from code
- 🧪 Easy testing - Test APIs without writing scripts
- 🤝 Better collaboration - Share interactive docs with team

**For Learning:**
- 📚 Understand API design patterns
- 🔍 See how REST APIs work in practice
- 🎯 Learn about HTTP methods, status codes, and headers
- 💡 Explore complex queries and aggregations

**For Production:**
- 📖 Professional documentation for clients
- 🔄 API specification export (OpenAPI JSON)
- 🛠️ Client code generation support
- 📊 API analytics and monitoring ready

---

## 📚 Manual API Documentation

If you prefer traditional documentation or need curl examples, here are the detailed API references:

---

### Authentication Endpoints

#### 1. Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "passenger1",
  "password": "password123",
  "role": "ROLE_USER"
}
```

**Roles:**
- `ROLE_USER` - Passenger (can create rides)
- `ROLE_DRIVER` - Driver (can accept and complete rides)

#### 2. Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "passenger1",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Use this token in all subsequent requests:**
```
Authorization: Bearer <your-token-here>
```

---

### Core Ride Endpoints

#### 3. Create Ride (Passenger)
```http
POST /api/rides
Authorization: Bearer <passenger-token>
Content-Type: application/json

{
  "pickupLocation": "Downtown Station",
  "dropLocation": "Airport Terminal 2",
  "fare": 25.50,
  "distanceKm": 15.3
}
```

#### 4. Accept Ride (Driver Only)
```http
POST /api/rides/accept/{rideId}
Authorization: Bearer <driver-token>
```

#### 5. Complete Ride (Driver or Passenger)
```http
POST /api/rides/complete/{rideId}
Authorization: Bearer <token>
```

#### 6. Get All Rides
```http
GET /api/rides
Authorization: Bearer <token>
```

---

### Advanced Query Endpoints

#### 7. Search Rides by Keyword
```http
GET /api/rides/search?text=Airport
Authorization: Bearer <token>
```

#### 8. Filter by Distance Range
```http
GET /api/rides/filter-distance?min=5&max=20
Authorization: Bearer <token>
```

#### 9. Filter by Date Range
```http
GET /api/rides/filter-date-range?start=2025-12-01&end=2025-12-31
Authorization: Bearer <token>
```

#### 10. Sort by Fare
```http
GET /api/rides/sort?order=asc
Authorization: Bearer <token>
```

#### 11. Advanced Search with Pagination
```http
GET /api/rides/advanced-search?search=Airport&status=REQUESTED&sort=fare&order=asc&page=0&size=10
Authorization: Bearer <token>
```

---

### Analytics Endpoints

#### 12. Driver Earnings
```http
GET /api/analytics/driver/{driverUsername}/earnings
Authorization: Bearer <token>
```

#### 13. Rides Per Day
```http
GET /api/analytics/rides-per-day
Authorization: Bearer <token>
```

#### 14. Driver Summary
```http
GET /api/analytics/driver/{driverUsername}/summary
Authorization: Bearer <token>
```

#### 15. User Spending
```http
GET /api/analytics/user/{username}/spending
Authorization: Bearer <token>
```

#### 16. Status Summary
```http
GET /api/analytics/status-summary
Authorization: Bearer <token>
```

---

## 🧪 Testing with Postman

### Import Postman Collection (Recommended)

1. Open Postman
2. Click "Import"
3. Import the provided collection (if available)

### Manual Testing Flow

1. **Register a Passenger:**
   - POST `/api/auth/register` with `"role": "ROLE_USER"`
   - Save the response

2. **Register a Driver:**
   - POST `/api/auth/register` with `"role": "ROLE_DRIVER"`
   - Save the response

3. **Login as Passenger:**
   - POST `/api/auth/login`
   - Copy the JWT token

4. **Create a Ride:**
   - POST `/api/rides` with passenger token
   - Save the ride ID from response

5. **Login as Driver:**
   - POST `/api/auth/login` with driver credentials
   - Copy the JWT token

6. **Accept the Ride:**
   - POST `/api/rides/accept/{rideId}` with driver token

7. **Complete the Ride:**
   - POST `/api/rides/complete/{rideId}` with driver token

8. **Check Analytics:**
   - GET `/api/analytics/driver/{driverUsername}/earnings`

---

## 🏗️ Project Structure

```
src/
 └── main/
     └── java/
         └── org/example/uberbackend/
             ├── config/              # Security & configuration
             │   ├── SecurityConfig.java
             │   ├── JwtFilter.java
             │   └── GlobalExceptionHandler.java
             ├── controller/          # REST API endpoints
             │   ├── AuthController.java
             │   ├── RideController.java
             │   ├── AnalyticsController.java
             │   └── HealthController.java
             ├── dto/                 # Data Transfer Objects
             │   └── AuthRequest.java
             ├── model/               # MongoDB entities
             │   ├── User.java
             │   └── Ride.java
             ├── repository/          # MongoDB repositories
             │   ├── UserRepository.java
             │   └── RideRepository.java
             ├── service/             # Business logic
             │   ├── UserService.java
             │   ├── RideService.java
             │   └── AnalyticsService.java
             └── util/                # Utility classes
                 └── JwtUtil.java
```

---

## 🔒 Security Best Practices

1. **Never commit `application.properties`** - Contains sensitive credentials
2. **Use strong JWT secrets** - Minimum 32 characters, random
3. **Change default passwords** - Don't use example credentials
4. **Whitelist IPs properly** - Don't use 0.0.0.0/0 in production
5. **Use HTTPS in production** - Never send JWT over HTTP
6. **Rotate JWT secrets periodically** - Update in production

---

## 🐛 Troubleshooting

### Problem: Application fails to start

**Solution:**
1. Check if MongoDB is running
2. Verify connection string in `application.properties`
3. Check if port 8081 is available
4. Check Java version: `java -version` (should be 17+)

### Problem: "Could not authenticate" error

**Solution:**
1. Verify MongoDB username and password
2. Check if IP is whitelisted in MongoDB Atlas
3. Ensure connection string format is correct

### Problem: "401 Unauthorized" on API calls

**Solution:**
1. Ensure you're using a valid JWT token
2. Token format: `Authorization: Bearer <token>`
3. Check token expiration (24 hours by default)
4. Re-login to get a fresh token

### Problem: "403 Forbidden" when accepting rides

**Solution:**
- Only users with `ROLE_DRIVER` can accept rides
- Ensure you registered with the correct role
- Check the JWT token contains the correct role

---

## 📊 Database Schema

### Users Collection
```json
{
  "_id": "ObjectId",
  "username": "string (unique)",
  "password": "string (bcrypt hash)",
  "role": "ROLE_USER | ROLE_DRIVER"
}
```

### Rides Collection
```json
{
  "_id": "ObjectId",
  "passengerUsername": "string",
  "driverUsername": "string",
  "pickupLocation": "string",
  "dropLocation": "string",
  "fare": "double",
  "distanceKm": "double",
  "status": "REQUESTED | ACCEPTED | COMPLETED",
  "createdDate": "LocalDate",
  "createdAt": "LocalDateTime"
}
```

---

## 🎯 Key Concepts Demonstrated

- Spring Boot REST API development
- JWT authentication and authorization
- MongoDB integration with Spring Data
- Aggregation pipelines for analytics
- Complex query operations (OR, AND, Range, Regex)
- Pagination and sorting
- Transaction management
- Global exception handling
- Role-based access control
- MVC architecture
- **Interactive API documentation with Swagger/OpenAPI 3.0**

---

## 📝 License

This is an educational project developed as part of a Spring Boot learning course.

---

## 🎉 Acknowledgments

Built as part of an in-class Spring Boot project to demonstrate:
- Professional backend development practices
- RESTful API design
- NoSQL database integration
- Modern authentication patterns
- Advanced query operations
- Interactive API documentation with Swagger/OpenAPI

---

## 🔗 Quick Links

- **Swagger UI:** http://localhost:8081/swagger-ui.html
- **OpenAPI Spec:** http://localhost:8081/api-docs
- **Health Check:** http://localhost:8081/api/health/ping

---

**Happy Coding! 🚀**

**💡 Tip:** Start with Swagger UI to explore and test all 23 APIs interactively!

