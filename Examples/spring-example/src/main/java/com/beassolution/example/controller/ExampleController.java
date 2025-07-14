package com.beassolution.example.controller;

import com.beassolution.ruleengine.annotation.EvaluateRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Example REST controller demonstrating the use of BeasRuleEngineClientSDK.
 * <p>
 * Shows how to annotate endpoints with @EvaluateRule for automatic rule validation.
 *
 * @author BEAS Solution Team
 * @since 1.0
 */
@RestController("/example")
public class ExampleController {
    /**
     * Example business operation endpoint.
     * <p>
     * This endpoint is protected by the @EvaluateRule annotation, which triggers rule validation
     * before the method executes. If the rule is invalid, an exception is thrown and the request fails.
     *
     * @param requestPayloadMap The request payload
     * @return HTTP 200 OK if the rule is valid
     */
    @PostMapping(path = "/business-operation")
    @EvaluateRule(ruleName = "SpringBootRule", throwIfInvalid = true)
    public ResponseEntity<HttpStatus> example(@RequestBody Map<String, Object> requestPayloadMap){
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
