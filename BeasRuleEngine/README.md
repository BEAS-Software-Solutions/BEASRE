# Beas Rule Engine

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-5.0+-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-BEAS%20License-blue.svg)](https://beassolution.com/rule-engine-license)

A powerful, flexible, and scalable rule engine built with Spring Boot 3 and Java 17, designed for complex business logic evaluation and execution.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)
- [Support](#support)

## 🎯 Overview

The Beas Rule Engine is a high-performance rule evaluation system that allows you to define, manage, and execute complex business rules using MVEL (MVFLEX Expression Language). It provides a RESTful API for rule evaluation, comprehensive caching mechanisms, and robust security features.

### Key Capabilities

- **Dynamic Rule Execution**: Execute rules on-the-fly with parameterized inputs
- **Caching System**: Multi-level caching for improved performance
- **Security**: OAuth2 integration with Keycloak for authentication
- **Scalability**: Asynchronous processing and MongoDB persistence
- **Extensibility**: Plugin-based architecture for custom functions and helpers

## ✨ Features

### Core Features
- 🚀 **High Performance**: Optimized rule compilation and execution
- 🔄 **Caching**: Intelligent caching of compiled rules and variables
- 🔒 **Security**: OAuth2 authentication and authorization
- 📊 **Monitoring**: Comprehensive logging and metrics
- 🔧 **Flexibility**: Support for custom functions and helper classes

### Technical Features
- **Java 17**: Latest LTS version with modern language features
- **Spring Boot 3.3.4**: Latest stable Spring Boot version
- **MongoDB**: NoSQL database for flexible data storage
- **MVEL**: Powerful expression language for rule definition
- **Swagger/OpenAPI**: Interactive API documentation
- **Docker**: Containerized deployment support

## 🏗️ Architecture

The Beas Rule Engine follows a layered architecture pattern:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   Controllers   │  │   DTOs          │  │   Validation │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                     Business Layer                          │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   Services      │  │   Engine        │  │   Cache      │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                     Data Layer                              │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   Repositories  │  │   Models        │  │   MongoDB    │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### Core Components

1. **Controllers**: REST API endpoints for rule operations
2. **Services**: Business logic and rule management
3. **Engine**: Rule compilation and execution engine
4. **Cache**: Multi-level caching system
5. **Models**: Data entities and DTOs
6. **Repositories**: Data access layer

## 📋 Prerequisites

Before running the Beas Rule Engine, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6** or higher
- **MongoDB 5.0** or higher
- **Keycloak** (for authentication - optional for development)

### System Requirements

- **Memory**: Minimum 2GB RAM, Recommended 4GB+
- **Storage**: Minimum 1GB free space
- **Network**: Port 8070 available (configurable)

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/your-org/BeasRuleEngine.git
cd BeasRuleEngine
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

#### Option A: Using Maven
```bash
mvn spring-boot:run
```

#### Option B: Using JAR
```bash
java -jar target/rule-engine-0.0.1.jar
```

#### Option C: Using Docker
```bash
docker build -t beas-rule-engine .
docker run -p 8070:8070 beas-rule-engine
```

## ⚙️ Configuration

### Application Properties

The main configuration file is located at `src/main/resources/application.yaml`:

```yaml
# Rule Engine Configuration
rule:
  container:
    name: general

# Cryptography Configuration
cryptography:
  key: "your-encryption-key"
  iv: "your-iv-vector"

# Spring Configuration
spring:
  application:
    name: beasre
  data:
    mongodb:
      host: localhost
      port: 27017
      database: beasre
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://keycloak.beassolution.com/realms/beas-rule-engine

# Server Configuration
server:
  port: 8070
  servlet:
    context-path: /beasre/v1
```

### Environment Variables

You can override configuration using environment variables:

```bash
export SPRING_DATA_MONGODB_HOST=your-mongodb-host
export SPRING_DATA_MONGODB_PORT=27017
export SERVER_PORT=8070
export CRYPTOGRAPHY_KEY=your-key
```

## 📖 Usage

### 1. Starting the Application

After installation, the application will be available at:
- **Base URL**: `http://localhost:8070/beasre/v1`
- **Swagger UI**: `http://localhost:8070/beasre/v1/swagger-ui.html`
- **Health Check**: `http://localhost:8070/beasre/v1/actuator/health`

### 2. Rule Evaluation

#### Basic Rule Evaluation

```bash
curl -X POST "http://localhost:8070/beasre/v1/rule-engine/evaluate" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-token" \
  -d '{
    "ruleName": "discount-rule",
    "parameters": {
      "customerType": "premium",
      "orderAmount": 1000
    },
    "payload": {
      "orderId": "12345",
      "items": ["item1", "item2"]
    }
  }'
```

#### Response Example

```json
{
  "response": {
    "discount": 0.15,
    "finalAmount": 850.0,
    "message": "Premium customer discount applied"
  },
  "status": {
    "message": "OK",
    "description": "Validation Executed"
  }
}
```

### 3. Cache Management

#### Sync Caches

```bash
curl -X GET "http://localhost:8070/beasre/v1/rule-engine/sync" \
  -H "Authorization: Bearer your-token"
```

### 4. Rule Management

The engine supports various rule management operations through dedicated controllers:

- **Rule Library**: Create, read, update, delete rule libraries
- **Function Library**: Manage utility functions
- **Rule Helper**: Manage helper classes

## 📚 API Documentation

### Interactive Documentation

Once the application is running, you can access the interactive API documentation:

- **Swagger UI**: `http://localhost:8070/beasre/v1/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8070/beasre/v1/v3/api-docs`

### Main Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/rule-engine/evaluate` | Evaluate a rule with parameters |
| `GET` | `/rule-engine/sync` | Synchronize all caches |
| `GET` | `/rule-library` | Get all rule libraries |
| `POST` | `/rule-library` | Create a new rule library |
| `PUT` | `/rule-library/{id}` | Update a rule library |
| `DELETE` | `/rule-library/{id}` | Delete a rule library |

### Authentication

The API uses OAuth2 with JWT tokens. Include the token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## 🧪 Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=RuleEngineTest

# Run tests in parallel
mvn test -Dparallel=methods -DthreadCount=4
```

### Test Coverage

The project includes comprehensive test coverage:

- **Unit Tests**: Individual component testing
- **Integration Tests**: API endpoint testing
- **Performance Tests**: Load and stress testing
- **Security Tests**: Authentication and authorization testing

Current test coverage: **85%+**

### Test Configuration

Test-specific configuration is in `src/test/resources/application-test.yaml`:

```yaml
spring:
  data:
    mongodb:
      database: beasre-test
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/test
```

## 🚀 Deployment

### Docker Deployment

1. **Build Docker Image**
```bash
docker build -t beas-rule-engine:latest .
```

2. **Run Container**
```bash
docker run -d \
  --name beas-rule-engine \
  -p 8070:8070 \
  -e SPRING_DATA_MONGODB_HOST=your-mongodb-host \
  -e CRYPTOGRAPHY_KEY=your-key \
  beas-rule-engine:latest
```

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: beas-rule-engine
spec:
  replicas: 3
  selector:
    matchLabels:
      app: beas-rule-engine
  template:
    metadata:
      labels:
        app: beas-rule-engine
    spec:
      containers:
      - name: beas-rule-engine
        image: beas-rule-engine:latest
        ports:
        - containerPort: 8070
        env:
        - name: SPRING_DATA_MONGODB_HOST
          value: "mongodb-service"
        - name: CRYPTOGRAPHY_KEY
          valueFrom:
            secretKeyRef:
              name: beas-secrets
              key: crypto-key
```

### Production Considerations

1. **Security**
   - Use strong encryption keys
   - Enable HTTPS
   - Configure proper CORS policies
   - Implement rate limiting

2. **Performance**
   - Configure appropriate JVM options
   - Set up MongoDB indexes
   - Enable caching
   - Use load balancers

3. **Monitoring**
   - Set up application monitoring
   - Configure logging aggregation
   - Set up health checks
   - Monitor cache performance

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes**
4. **Add tests for new functionality**
5. **Ensure all tests pass**
   ```bash
   mvn clean test
   ```
6. **Commit your changes**
   ```bash
   git commit -m "Add feature: description of changes"
   ```
7. **Push to the branch**
   ```bash
   git push origin feature/your-feature-name
   ```
8. **Create a Pull Request**

### Development Guidelines

- Follow Java 17 coding standards
- Use meaningful variable and method names
- Add comprehensive JavaDoc comments
- Write unit tests for all new functionality
- Follow the existing code style and formatting

## 📄 License

This project is licensed under the BEAS License. See the [LICENSE](LICENSE) file for details.

**Copyright © 2024 BEAS Software Solution. All rights reserved.**

## 🆘 Support

### Getting Help

- **Documentation**: Check this README and API documentation
- **Issues**: Report bugs and feature requests on GitHub
- **Email**: contact@beassolution.com
- **Website**: https://beassolution.com

### Community

- **GitHub Discussions**: Share ideas and ask questions
- **Stack Overflow**: Tag questions with `beas-rule-engine`
- **Slack**: Join our community channel

### Commercial Support

For enterprise support, custom development, and consulting services:

- **Email**: enterprise@beassolution.com
- **Phone**: +1 (555) 123-4567
- **Website**: https://beassolution.com/enterprise

---

**Made with ❤️ by the BEAS Software Solution Team**