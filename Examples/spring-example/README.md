# Spring Example Project with BeasRuleEngineClientSDK

This project demonstrates how to integrate and use the **BeasRuleEngineClientSDK** in a real Spring Boot application. It provides a working example of how to configure, inject, and use the SDK for rule evaluation and validation in your own projects.

## Prerequisites
- Java 17+
- Maven 3.8+
- Access to a running BEAS Rule Engine backend
- BeasRuleEngineClientSDK deployed to your Maven repository (see its own README)

## 1. Add the SDK Dependency
Add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>com.beassolution.client</groupId>
    <artifactId>BeasRuleEngineClientSDK</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 2. Configuration
Add the following properties to your `application.yaml` or `application.properties`:

```yaml
rule:
  engine:
    enabled: true
    base-url: http://localhost:8080 # URL of your BEAS Rule Engine backend
    use-client-token: false         # Set true to use client token, false for Keycloak
    # ... other properties ...
```

## 3. Enable Component Scan for the SDK
Create a configuration class (already included as `BeasRuleEngineSDKScanConfig.java`):

```java
@Configuration
@ComponentScan(basePackages = {"com.beassolution.ruleengine"})
public class BeasRuleEngineSDKScanConfig {
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}
```
This ensures that all SDK beans (services, properties, etc.) are discovered and injected by Spring.

## 4. Example Controller Usage
See `ExampleController.java` for a real endpoint using the SDK:

```java
@RestController("/example")
public class ExampleController {
    @PostMapping(path = "/business-operation")
    @EvaluateRule(ruleName = "SpringBootRule", throwIfInvalid = true)
    public ResponseEntity<HttpStatus> example(@RequestBody Map<String, Object> requestPayloadMap){
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
```

### How It Works
- The `@EvaluateRule` annotation triggers the SDK's aspect before the endpoint executes.
- The aspect automatically calls the rule engine with the provided rule name and request data.
- If the rule is invalid and `throwIfInvalid=true`, an exception is thrown and the request fails.
- If the rule is valid, the endpoint logic proceeds as normal.

## 5. Running the Example
1. Make sure your BEAS Rule Engine backend is running and accessible.
2. Build and run this project:
   ```sh
   mvn clean package
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```
3. Test the endpoint (e.g. with curl or Postman):
   ```sh
   curl -X POST http://localhost:8081/example/business-operation \
        -H "Content-Type: application/json" \
        -d '{"field": "value"}'
   ```

## 6. Error Handling
- All exceptions are handled globally by `GeneralAdvice.java`.
- If rule validation fails, a standardized error response is returned.

## 7. Customization
- You can add more endpoints and annotate them with `@EvaluateRule` to protect business logic with rule validation.
- You can inject and use the SDK directly for advanced scenarios.

## License
This project is for demonstration purposes. See the main repository for license details. 