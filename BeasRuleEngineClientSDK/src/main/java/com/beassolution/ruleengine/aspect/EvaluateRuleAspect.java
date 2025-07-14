package com.beassolution.ruleengine.aspect;

import com.beassolution.ruleengine.annotation.EvaluateRule;
import com.beassolution.ruleengine.model.response.RuleEvaluateResponse;
import com.beassolution.ruleengine.properties.RuleEngineProperties;
import com.beassolution.ruleengine.service.RuleEngineClient;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
@Order(1)
public class EvaluateRuleAspect {

    private final RuleEngineClient ruleEngineClient;
    private final RuleEngineProperties properties;

    public EvaluateRuleAspect(RuleEngineClient ruleEngineClient, RuleEngineProperties properties) {
        this.ruleEngineClient = ruleEngineClient;
        this.properties = properties;
    }

    @Before("@annotation(com.beassolution.ruleengine.annotation.EvaluateRule)")
    public void evaluate(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        EvaluateRule requestedAnnotation = method.getAnnotation(EvaluateRule.class);

        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();

        Map<String, Object> requestBodyMap = new HashMap<>();
        Map<String, Object> requestParamMap = new HashMap<>();

        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation annotation : paramAnnotations[i]) {
                if (annotation instanceof org.springframework.web.bind.annotation.RequestBody) {
                    requestBodyMap.put(paramNames[i], args[i]);
                }
                if (annotation instanceof org.springframework.web.bind.annotation.RequestParam) {
                    requestParamMap.put(paramNames[i], args[i]);
                }
            }
        }

        ResponseEntity<RuleEvaluateResponse> response = ruleEngineClient.evaluate(requestedAnnotation.ruleName(), requestParamMap, requestBodyMap);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Rule engine validation error : " + response);
        }
        RuleEvaluateResponse ruleEvaluateResponse = response.getBody();
        if (requestedAnnotation.throwIfInvalid()) {
            Object bodyResponse = ruleEvaluateResponse.getResponse();
            if (!(bodyResponse instanceof Boolean) || !((Boolean) bodyResponse)) {
                throw new RuntimeException("Rule validation failed , Rule=" + requestedAnnotation.ruleName());
            }
        }


    }
}
