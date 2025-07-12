# 🧠 BeasRuleEngine & BeasRuleEngineUI

> Open-source, MVEL-based, dynamic rule engine platform with REST support.  
> Define rules via UI or SDK, attach external JARs, and evaluate dynamically with custom payloads.

---

## 🚀 Quick Start

### Backend – BeasRuleEngine

```bash
git clone -b dev https://github.com/BEAS-Software-Solutions/BeasRuleEngine.git
cd BeasRuleEngine
mvn clean install
java -jar target/BeasRuleEngine.jar
```

> Backend runs at: `http://localhost:8080`

---

### Frontend – BeasRuleEngineUI

```bash
git clone -b dev https://github.com/BEAS-Software-Solutions/BeasRuleEngineUI.git
cd BeasRuleEngineUI
npm install
npm start
```

> UI runs at: `http://localhost:3000`

---

## ⚙️ Architecture Overview

```
[ User ]
   ↓
[ React UI ]
   ↓ REST API
[ Spring Boot Backend ]
   ├─ Function / Helper loader
   ├─ MVEL rule pre-compiler & cache
   └─ RuleEvaluate REST endpoint
```

- MVEL functions are embedded  
- Classes from external JARs are instantiated and added to context  
- Rules are precompiled and cached  
- Payload is evaluated using cached rules

---

## 📘 Usage Flow

### 1. Add Function Definition
- Define MVEL functions (e.g., `applyDiscount()`) via UI.

### 2. Load Helper JAR
- Attach external JARs via the UI to extend rule capabilities.

### 3. Write Rule in MVEL

```mvel
if (customer.type == "VIP" && amount > 100) {
  return applyDiscount(amount);
}
```

### 4. Sync Rule Engine
- Compile and cache rules and attach helpers.

### 5. Evaluate Rule via REST API

```http
POST /api/evaluate
Content-Type: application/json

{
  "ruleId": "DiscountRule",
  "payload": {
    "customer": { "type": "VIP" },
    "amount": 200
  }
}
```

---

## 🔌 REST API

> Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Sample Request

```bash
curl -X POST http://localhost:8080/api/evaluate   -H 'Content-Type: application/json'   -d '{"ruleId":"DiscountRule","payload":{"customer":{"type":"VIP"},"amount":150}}'
```

---

## 🔧 Developer Guide

### Backend Components

| Component           | Description                                                    |
|---------------------|----------------------------------------------------------------|
| `FunctionRegistry`  | Stores registered MVEL functions                               |
| `HelperLoader`      | Loads classes from external JARs into the evaluation context   |
| `RuleCache`         | Precompiles and caches rules                                   |
| `RuleEngineService` | Processes evaluation requests and returns output               |

### Frontend Overview

- Built with React and Material UI
- Includes 3 core views:
  - **Function Editor**
  - **Helper Manager**
  - **Rule Editor with Sync Button**

### Embedded SDK Usage (Java)

```java
RuleEngine engine = RuleEngine.builder()
    .withFunctionRegistry(functionRegistry)
    .withHelperLoader(helperLoader)
    .withCachedRules(ruleCache)
    .build();

Object result = engine.evaluate("DiscountRule", payloadMap);
```

---

## 🧪 Examples

If you add `discount-rule.mvel` and `test-payload.json` to your project:

```bash
curl -X POST http://localhost:8080/api/evaluate   -H 'Content-Type: application/json'   -d @examples/test-payload.json
```

---

## 🤝 Contributing

1. Fork & Clone the repo
2. Create a new branch:
   ```bash
   git checkout -b feature/your-feature
   ```
3. Run tests before PR:
   ```bash
   mvn test
   ```
4. Submit your Pull Request!

---

## 📄 License

[MIT](LICENSE)

---

## 📬 Contact

**BEAS Software Solutions**  
🌐 Website: [https://beassolution.com](https://beassolution.com)  
✉️ Email: info@beassolution.com  
📍 Location: Istanbul, Türkiye