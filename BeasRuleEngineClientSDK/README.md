# BeasRuleEngineClientSDK

A modern Java SDK for integrating with the BEAS Rule Engine backend. This SDK provides both synchronous and asynchronous methods for evaluating rules, handling authentication, and managing configuration in Spring Boot applications.

## Features
- Synchronous and asynchronous rule evaluation
- Keycloak or client credentials authentication
- Spring Boot auto-configuration support
- Detailed error handling and logging
- Model classes for standardized API responses

## Installation
Add the following dependency to your Maven `pom.xml`:

```xml
<dependency>
    <groupId>com.beassolution.client</groupId>
    <artifactId>BeasRuleEngineClientSDK</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Configuration
Add the following properties to your `application.yaml` or `application.properties`:

```yaml
rule:
  engine:
    enabled: true
    base-url: http://localhost:8080 # URL of your BEAS Rule Engine backend
    use-client-token: false         # Set true to use client token, false for Keycloak
    # ... other properties ...
```

## Usage Example

```java
@Autowired
private RuleEngineClient ruleEngineClient;

Map<String, Object> params = new HashMap<>();
params.put("param1", "value1");
Map<String, Object> payload = new HashMap<>();
payload.put("field", "data");

// Synchronous
ResponseEntity<RuleEvaluateResponse> response = ruleEngineClient.evaluate("myRule", params, payload);

// Asynchronous
ruleEngineClient.evaluateAsync("myRule", params, payload)
    .subscribe(result -> System.out.println(result));
```

## Main Classes

### RuleEngineClient
- `evaluate(String ruleName, Map<String, Object> params, Map<String, Object> payload)`
  - Synchronously evaluates a rule.
- `evaluateAsync(String ruleName, Map<String, Object> params, Map<String, Object> payload)`
  - Asynchronously evaluates a rule (returns `Mono<RuleEvaluateResponse>`).

### Auto-Configuration
- `RuleEngineAutoConfiguration` provides Spring Boot auto-configuration for all required beans.

### Model Classes
- `BaseResponseModel<T>`: Generic wrapper for API responses.
- `BaseResponse`: Standardized status and error information.
- `RuleEvaluateRequest`, `RuleEvaluateResponse`: Request/response payloads for rule evaluation.

## Integration
- Add the SDK as a dependency.
- Ensure your Spring Boot app scans the `com.beassolution.ruleengine` package (see example config below):

```java
@Configuration
@ComponentScan(basePackages = {"com.beassolution.ruleengine"})
public class BeasRuleEngineSDKScanConfig {}
```

- Configure authentication and endpoint URLs as needed.

## Contribution
Contributions are welcome! Please fork the repository and submit pull requests. For major changes, open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License - see the LICENSE file for details. 