# BEAS Rule Engine

BEAS Rule Engine is a modern web application developed for rule-based business logic management. It is designed with a microservice architecture consisting of Spring Boot backend and React frontend.


## 📸 Screenshots

### 🔐 Authentication
Log in from any OAuth system you have integrated with the system.
![Login page](screenshots/01-keycloak-login.png)

### 📋 Rules Management
Rules List Page
![Rules page](screenshots/02-rule-screen.png)

### 🔧 Helper Management
Helper Page
![Helpers page](screenshots/03-create-edit-helper.png)

### ⚙️ Function Management
Functions Page
![Functions page](screenshots/04-create-edit-functions.png)

### 📝 Rule Creation
Create Rule Page
![Create Rule page](screenshots/05-create-edit-rule.png)

### 🔄 Engine Synchronization
Synchronization of the Rules Engine
![Synch engine](screenshots/06-synch-engine.png)

### 🔑 Token Management
Get Token
![Retrieve token](screenshots/07-get-token.png)

**Get Access Token:**
```bash
curl --location 'https://keycloak.beassolution.com/realms/beas-rule-engine/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=beasre' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=test' \
--data-urlencode 'password=test'
```

### 🔄 Sync Operations
Sync Request
![Sync request](screenshots/08-sync-request.png)

**Synchronize Rule Engine:**
```bash
curl --location 'http://localhost:8070/beasre/v1/rule-engine/sync' \
--header 'Authorization: Bearer eyJhbG....g41edg'
```
*Note: Token is shortened for display purposes*

### 🧪 Rule Evaluation
Sample evaluation
![Sample evaluation](screenshots/09-sample-rule-evaluate.png)

**Request:**
```bash
curl --location 'http://localhost:8070/beasre/v1/rule-engine/evaluate' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOi....g41edg' \
--data '{
    "ruleName": "SampleRule",
    "parameters": {
        "age": 20,
        "name": "Serdar",
        "surname": "TURKEL"
    },
    "payload": {
        "sourceSystem": "company",
        "note": "SBEAS",
        "subObject":{
            "name":"SBEAS"
        }
    }
}'
```

**Response:**
```json
{
    "status": {
        "errorCode": 0,
        "status": "OK",
        "message": "Validation Executed",
        "infoList": null
    },
    "response": {
        "duplication": {
            "result": false,
            "input": [
                "BEAS",
                "BEAS",
                "SOFTWARE",
                "SOLUTIONS"
            ]
        }
    }
}
```




## 🏗️ Project Structure

```
BEASRE/
├── BeasRuleEngine/          # Spring Boot Backend Application
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── Jenkinsfile
├── BeasRuleEngineUI/        # React Frontend Application
│   ├── src/
│   ├── package.json
│   ├── Dockerfile
│   └── Jenkinsfile
└── README.md
```

## 🚀 Technologies

### Backend (BeasRuleEngine)
- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Security + OAuth2**
- **Spring Data MongoDB**
- **MVEL2** (Rule Engine)
- **RSQL Parser** (Query Language)
- **ModelMapper**
- **ClassGraph** (Dynamic Class Loading)
- **Lombok**

### Frontend (BeasRuleEngineUI)
- **React 18**
- **Node.js 23.1.0**
- **Material-UI**
- **OIDC Authentication**
- **Axios** (HTTP Client)

### DevOps
- **Docker**
- **Jenkins** (CI/CD)
- **Nexus Repository**
- **Nginx** (Web Server)

## 📋 Features

### 🔧 Rule Engine Features
- **Dynamic Rule Management**: MVEL2-based rule engine
- **Helper Class Support**: Dynamic class loading from JAR files
- **Function Libraries**: Reusable MVEL functions
- **Cache Mechanism**: Multi-layer cache for performance optimization
- **RSQL Querying**: Advanced filtering and search capabilities

### 🔐 Security
- **OAuth2 Resource Server**: JWT-based authentication
- **Field-Level Encryption**: Sensitive data encryption
- **Role-Based Access Control**: Role-based access control

### 📊 Data Management
- **MongoDB**: NoSQL database support
- **Audit Trail**: Automatic audit logging
- **Soft Delete**: Soft deletion operations

## 🛠️ Installation

### Prerequisites
- Java 17+
- Node.js 23.1.0+
- Docker & Docker Compose
- MongoDB 5.0+
- Maven 3.8+

### Backend Installation

```bash
# Navigate to BeasRuleEngine directory
cd BeasRuleEngine

# Install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend application will run on `http://localhost:8090` by default.

### Frontend Installation

```bash
# Navigate to BeasRuleEngineUI directory
cd BeasRuleEngineUI

# Install dependencies
npm install

# Run in development mode
npm start
```

The frontend application will run on `http://localhost:3000` by default.

### Docker Installation

```bash
# Build backend Docker image
cd BeasRuleEngine
docker build -t beas-rule-engine .

# Build frontend Docker image
cd ../BeasRuleEngineUI
docker build -t beas-rule-engine-ui .

# Run applications
docker run -p 8090:8090 beas-rule-engine
docker run -p 3000:80 beas-rule-engine-ui
```

## 🔧 Configuration

### Backend Configuration

Configure the following settings in `BeasRuleEngine/src/main/resources/application.yaml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/beas-rule-engine
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://keycloak.beassolution.com/realms/beas-rule-engine

server:
  port: 8090
```

### Frontend Configuration

Configure OIDC settings in `BeasRuleEngineUI/src/config/oidcConfig.js`:

```javascript
export const oidcConfig = {
  authority: 'https://keycloak.beassolution.com/realms/beas-rule-engine',
  client_id: 'beasre',
  redirect_uri: 'http://localhost:3000/callback',
  // ... other settings
};
```

## 📚 API Documentation

After the application starts, you can access Swagger UI at:
- **Swagger UI**: `http://localhost:8090/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8090/v3/api-docs`

## 🧪 Testing

### Backend Tests

```bash
cd BeasRuleEngine
mvn test
```

### Frontend Tests

```bash
cd BeasRuleEngineUI
npm test
```

## 🚀 Deployment

### Jenkins Pipeline

Both projects are automatically built and deployed to Nexus repository using Jenkins pipelines:

- **Backend Pipeline**: `BeasRuleEngine/Jenkinsfile`
- **Frontend Pipeline**: `BeasRuleEngineUI/Jenkinsfile`

### Docker Deployment

```bash
# Production build
docker build -t beas-rule-engine:latest BeasRuleEngine/
docker build -t beas-rule-engine-ui:latest BeasRuleEngineUI/

# Deploy
docker run -d -p 8090:8090 --name beas-backend beas-rule-engine:latest
docker run -d -p 80:80 --name beas-frontend beas-rule-engine-ui:latest
```

## 📖 Usage

### Creating Rules

1. **Create Function Library**: Define MVEL functions
2. **Load Helper Classes**: Load helper classes from JAR files
3. **Create Rule Library**: Write main rule logic using MVEL
4. **Execute Rules**: Run rules via API

### API Usage

```bash
# Rule evaluation
curl -X POST http://localhost:8090/api/v1/rule-engine/evaluate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "ruleName": "example-rule",
    "payload": {"data": "test"}
  }'
```

## 🔍 Monitoring & Logging

- **Application Logs**: Spring Boot logging
- **Health Check**: `http://localhost:8090/actuator/health`
- **Metrics**: `http://localhost:8090/actuator/metrics`

## 🤝 Contributing

1. Fork the project
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the license specified in the [LICENSE](LICENSE) file.

## 📞 Contact

- **Project Owner**: BEAS Solution Team
- **Email**: info@beassolution.com
- **Website**: https://ruleapp.beassolution.com

## 🙏 Acknowledgments

This project is developed using the following open source projects:

- [Spring Boot](https://spring.io/projects/spring-boot)
- [React](https://reactjs.org/)
- [MVEL](http://mvel.documentnode.io/)
- [Material-UI](https://material-ui.com/)

---

**Note**: This README file explains the general structure of the project. For detailed technical documentation, please refer to the README files in each project directory. 
