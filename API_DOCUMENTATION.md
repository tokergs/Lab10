# API Documentation - Lab 10 + Lab 11-12 (JWT Auth + Access Control)

## Base URL
```
http://localhost:8080
```

## Authentication

This project uses **JWT token-based authentication**.

- Obtain a token via **POST** `/auth/login`
- Send it on protected requests via header:
  - `Authorization: Bearer <token>`

## Endpoints

### 0. Register
**POST** `/auth/register`

**Headers:**
- `Content-Type`: `application/json` (required)

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

**Response:**
- **201 Created**: returns created user (without password)
- **400 Bad Request**: validation errors

---

### 0. Login
**POST** `/auth/login`

**Headers:**
- `Content-Type`: `application/json` (required)

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

**Response:**
- **200 OK**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
- **400 Bad Request**: invalid credentials (safe message)

---

### 1. Get All Users
**GET** `/users`

**Headers:**
- `Accept` (optional): `application/json` (default)
- `Authorization`: `Bearer <token>` (required)

**Response:**
- **200 OK**: Returns a list of all users
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com"
  }
]
```

---

### 2. Get User by ID
**GET** `/users/{id}`

**Path Parameters:**
- `id` (Integer): User ID

**Headers:**
- `Accept` (optional): `application/json` (default)

**Response:**
- **200 OK**: Returns the user
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com"
}
```
- **404 Not Found**: User not found

---

### 3. Create User (JSON) (ADMIN only)
**POST** `/users`

**Headers:**
- `Content-Type`: `application/json` (required)
- `Authorization`: `Bearer <token>` (required)

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

**Validation Rules:**
- `username`: Required, 3-50 characters
- `email`: Required, valid email format
- `password`: Required, at least 8 characters, must contain uppercase, lowercase, and digit

**Response:**
- **201 Created**: User created successfully
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com"
}
```
- **400 Bad Request**: Validation errors
- **415 Unsupported Media Type**: Wrong Content-Type

---

### 4. Create User (Form Data)
**POST** `/users/form`

**Headers:**
- `Content-Type`: `application/x-www-form-urlencoded` (required)

**Request Body (form data):**
```
username=john_doe&email=john@example.com&password=SecurePass123
```

**Validation Rules:**
- Same as JSON endpoint

**Response:**
- **201 Created**: User created successfully
- **400 Bad Request**: Validation errors

---

### 5. Update User
**PUT** `/users/{id}`

**Path Parameters:**
- `id` (Integer): User ID

**Headers:**
- `Content-Type`: `application/json` (required)

**Request Body:**
```json
{
  "username": "john_doe_updated",
  "email": "john.updated@example.com",
  "password": "NewSecurePass123"
}
```

**Response:**
- **200 OK**: User updated successfully
- **400 Bad Request**: Validation errors
- **404 Not Found**: User not found

---

### 6. Delete User
**DELETE** `/users/{id}`

**Path Parameters:**
- `id` (Integer): User ID

**Response:**
- **200 OK**: User deleted successfully
```json
{
  "message": "User deleted successfully"
}
```
- **404 Not Found**: User not found

---

### 7. Current user
**GET** `/users/me`

**Headers:**
- `Authorization`: `Bearer <token>` (required)

**Response:**
- **200 OK**: current user profile

---

### 8. Notes (owner-only CRUD)

All `/notes/**` endpoints require:
- `Authorization`: `Bearer <token>`

**GET** `/notes` — list own notes

**POST** `/notes` — create note
```json
{
  "title": "My note",
  "content": "Text"
}
```

**GET** `/notes/{id}` — get own note (404 if чужая/нет)

**PUT** `/notes/{id}` — update own note

**DELETE** `/notes/{id}` — delete own note (204 No Content)

**GET** `/notes/search?q=abc` — raw SQL prepared-statement search (only own notes)

---

### 9. Hello
**GET** `/users/hello`

**Headers:**
- `User-Agent` (optional): Client user agent

**Response:**
- **200 OK**: Greeting message
```json
{
  "message": "Hello, user! Your User-Agent: Mozilla/5.0..."
}
```

---

## Error Responses

All error responses follow this structure:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/users",
  "validationErrors": [
    "username: Username must be between 3 and 50 characters",
    "email: Email should be valid",
    "password: Password must be at least 8 characters long..."
  ]
}
```

### Status Codes

- **200 OK**: Request successful
- **201 Created**: Resource created successfully
- **400 Bad Request**: Validation errors or invalid input
- **401 Unauthorized**: Authentication failed
- **404 Not Found**: Resource not found
- **415 Unsupported Media Type**: Wrong Content-Type header
- **500 Internal Server Error**: Server error

---

## Validation Rules

### Username
- Required
- Length: 3-50 characters

### Email
- Required
- Must be a valid email format

### Password (Custom Validator)
- Required
- Minimum 8 characters
- Must contain at least one uppercase letter
- Must contain at least one lowercase letter
- Must contain at least one digit

---

## Examples

### Register + Login + call protected endpoint (cURL)
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test_user",
    "email": "test@example.com",
    "password": "SecurePass123"
  }'

TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"SecurePass123"}' | jq -r .token)

curl -X GET http://localhost:8080/notes \
  -H "Authorization: Bearer '$TOKEN'"
```

### Create User (cURL)
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "username": "test_user",
    "email": "test@example.com",
    "password": "SecurePass123"
  }'
```

### Create User with Form Data (cURL)
```bash
curl -X POST http://localhost:8080/users/form \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=test_user&email=test@example.com&password=SecurePass123"
```

### Get User with Headers (cURL)
```bash
curl -X GET http://localhost:8080/users/1 \
  -H "Accept: application/json" \
  -H "User-Agent: MyApp/1.0"
```
