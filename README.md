# Recipe API

A comprehensive REST API for a recipe sharing platform with AI-powered features, built with Spring Boot 3.4.3.

## 📋 Description

The Recipe API is a full-featured backend service that enables users to create, share, and discover recipes. The platform includes user authentication, recipe management, social features like comments and favorites, and AI-powered recipe assistance.

### Key Features

- **User Management**: User registration, authentication, and profile management
- **Recipe Management**: Create, read, update, and delete recipes with detailed information
- **Category System**: Organize recipes by categories
- **Social Features**: Comment on recipes, add to favorites, and rate recipes
- **AI Integration**: Get recipe suggestions, improvements, and chat with an AI culinary assistant
- **Search & Discovery**: Search recipes by keywords, browse by categories, and filter by users
- **Pagination**: Efficient data handling with paginated responses

### Core Entities

- **Users**: Account management with role-based access (USER/ADMIN)
- **Recipes**: Complete recipe information including ingredients, instructions, timing, and difficulty
- **Categories**: Recipe categorization system
- **Comments**: Recipe reviews and ratings
- **Favorites**: Personal recipe collections
- **AI Features**: Intelligent recipe assistance and suggestions

## 🚀 Getting Started

### Prerequisites

- Java 17
- Gradle 7.0 or higher
- Mistral AI API key
- Optional: MySQL for production deployment

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <your-repository-url>
   cd recipe-api
   ```

2. **Configure Environment Variables (Optional)**
   The application comes with default configurations but you can override them:
   ```bash
   export MISTRAL_API_KEY=your_mistral_ai_api_key
   export JWT_SECRET=your_custom_jwt_secret
   ```

3. **Build the Project**
   ```bash
   ./gradlew clean build
   ```

4. **Run the Application**
   ```bash
   ./gradlew bootRun
   ```

The API will be available at `http://localhost:8080`

### Development Features

- **H2 Database Console**: Access at `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:recipeapidb`
    - Username: `sa`
    - Password: (empty)
- **Swagger UI**: Access API documentation at `http://localhost:8080/swagger-ui.html`
- **API Docs**: Raw OpenAPI spec at `http://localhost:8080/v3/api-docs`

### Database Configuration

The application uses H2 in-memory database for development with the following default settings:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:recipeapidb
    driver-class-name: org.h2.Driver
    username: sa
    password: ""
```

For production, update your `application.yml` or set environment variables for MySQL:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/recipe_db
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: update
```

The API will be available at `http://localhost:8080`

### Docker Setup (Alternative)

```bash
# Build the image
docker build -t recipe-api .

# Run with environment variables
docker run -p 8080:8080 \
  -e MISTRAL_API_KEY=your_key \
  -e JWT_SECRET=your_secret \
  recipe-api
```

## 🌐 Production Deployment

The API is deployed and accessible at:

**Production URL**: `https://recipe-api.railway.app`

> Deployment not working - won't do it

## 📚 API Documentation

### Authentication

The API uses JWT Bearer token authentication. Most endpoints require authentication except for:
- User registration (`POST /auth/register`)
- User login (`POST /auth/login`)
- Public recipe browsing (`GET /recipes`, `GET /recipes/{id}`)
- Category listing (`GET /categories`)

### Main Endpoints

#### Authentication
- `POST /auth/register` - Register a new user
- `POST /auth/login` - Login and receive JWT token

#### Recipes
- `GET /recipes` - Get all recipes (paginated)
- `POST /recipes` - Create a new recipe (requires auth)
- `GET /recipes/{id}` - Get recipe by ID
- `PUT /recipes/{id}` - Update recipe (requires auth)
- `DELETE /recipes/{id}` - Delete recipe (requires auth)
- `GET /recipes/search?keyword={keyword}` - Search recipes
- `GET /recipes/category/{categoryId}` - Get recipes by category
- `GET /recipes/user/{userId}` - Get recipes by user

#### Categories
- `GET /categories` - Get all categories
- `POST /categories` - Create category (Admin only)
- `PUT /categories/{id}` - Update category (Admin only)
- `DELETE /categories/{id}` - Delete category (Admin only)

#### Comments & Ratings
- `GET /recipes/{recipeId}/comments` - Get recipe comments
- `POST /recipes/{recipeId}/comments` - Add comment (requires auth)
- `PUT /comments/{id}` - Update comment (requires auth)
- `DELETE /comments/{id}` - Delete comment (requires auth)

#### Favorites
- `POST /favorites/recipe/{recipeId}` - Add to favorites (requires auth)
- `DELETE /favorites/recipe/{recipeId}` - Remove from favorites (requires auth)
- `GET /favorites/user/{userId}` - Get user's favorites (requires auth)

#### AI Features
- `POST /ai/suggest-recipe` - Get recipe suggestions (requires auth)
- `POST /ai/improve-recipe/{recipeId}` - Get recipe improvements (requires auth)
- `POST /ai/chat` - Chat with AI assistant (requires auth)

#### User Management
- `GET /users` - Get all users (Admin only)
- `GET /users/{id}` - Get user by ID (requires auth)
- `DELETE /users/{id}` - Delete user (Admin only)

### Request Examples

**Register User**
```json
POST /auth/register
{
  "username": "chef123",
  "email": "chef@example.com",
  "password": "securepassword"
}
```

**Create Recipe**
```json
POST /recipes
Authorization: Bearer <your-jwt-token>
{
  "title": "Chocolate Chip Cookies",
  "description": "Classic homemade cookies",
  "instructions": "1. Preheat oven to 375°F...",
  "preparationTime": 15,
  "cookingTime": 12,
  "servings": 24,
  "difficulty": "EASY",
  "categoryId": 1,
  "ingredients": [
    {
      "name": "All-purpose flour",
      "quantity": 2.25,
      "unit": "cups"
    },
    {
      "name": "Chocolate chips",
      "quantity": 2,
      "unit": "cups"
    }
  ]
}
```

**AI Recipe Suggestion**
```json
POST /ai/suggest-recipe
Authorization: Bearer <your-jwt-token>
{
  "ingredients": ["chicken", "rice", "vegetables"],
  "cuisine": "Asian",
  "difficulty": "MEDIUM",
  "maxTime": 45
}
```

## 🧪 Testing with Postman

A complete Postman collection is available for testing all API endpoints:

**Postman Collection**: [Recipe API Collection](https://github.com/ThomasSEGALEN/recipe-api/blob/master/RecipeAPIPostman.json)

The collection includes:
- Pre-configured requests for all endpoints
- Environment variables for easy switching between local and production
- Authentication examples
- Sample request bodies
- Test scripts for common scenarios

### Setting up Postman

1. Import the collection using the link above
2. Set up environment variables:
    - `base_url`: `http://localhost:8080` (local) or production URL
    - `jwt_token`: Your JWT token (obtained from login)
    - `user_id`: Your user ID (for authenticated requests)
    - `recipe_id`: Default recipe ID for testing
    - `category_id`: Default category ID for testing
3. Access the H2 console at `http://localhost:8080/h2-console` to view database content
4. View API documentation at `http://localhost:8080/swagger-ui.html`
5. Run the "Auth/Login" request first to get your token
6. The token will be automatically set for subsequent requests

## 🛠 Built With

- **Spring Boot 3.4.3** - Main framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **H2 Database** - In-memory database for development
- **MySQL** - Production database support
- **JWT (jjwt 0.12.6)** - Token-based authentication
- **Mistral AI (mistral-large-latest)** - AI features integration
- **SpringDoc OpenAPI 2.8.6** - API documentation
- **Spring Boot Actuator** - Application monitoring
- **Gradle** - Build tool and dependency management
- **Java 17** - Programming language

## 📖 API Schema

The API follows REST principles and returns JSON responses. All paginated endpoints return data in the following format:

```json
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0,
  "first": true,
  "last": false
}
```

Error responses follow the standard HTTP status codes with descriptive messages.

## 🔐 Security

- JWT-based authentication
- Role-based authorization (USER/ADMIN)
- Password encryption
- CORS configuration
- Input validation and sanitization
