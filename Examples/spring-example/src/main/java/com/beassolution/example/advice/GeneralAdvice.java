package com.beassolution.example.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collection;

/**
 * Global exception handler and response body advice for the Spring Example project.
 * <p>
 * Provides centralized exception handling and response processing for the entire application.
 * Converts various exceptions into standardized response formats and adds metadata to collection responses.
 *
 * <p>Key features include:
 * <ul>
 *   <li>Global exception handling</li>
 *   <li>Standardized error responses</li>
 *   <li>Collection response metadata</li>
 *   <li>Exception type conversion</li>
 * </ul>
 *
 * @author BEAS Solution Team
 * @version 1.0
 * @since 1.0
 */
@ControllerAdvice
@Slf4j
public class GeneralAdvice implements ResponseBodyAdvice<Collection<?>> {

    /**
     * Determines if the advice should be applied to the response.
     *
     * @param returnType    The method return type
     * @param converterType The message converter type
     * @return true if the response is a Collection
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Collection.class.isAssignableFrom(returnType.getParameterType());
    }

    /**
     * Adds metadata to collection responses.
     * <p>
     * Adds an X-Total-Count header to collection responses to provide information about the total number of items.
     *
     * @param body                  The response body
     * @param returnType            The method return type
     * @param selectedContentType   The selected content type
     * @param selectedConverterType The selected converter type
     * @param request               The HTTP request
     * @param response              The HTTP response
     * @return The modified response body
     */
    @Override
    public Collection<?> beforeBodyWrite(Collection<?> body, MethodParameter returnType, MediaType selectedContentType,
                                         Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                         ServerHttpResponse response) {
        int size = body != null ? body.size() : 0;
        response.getHeaders().add("X-Total-Count", String.valueOf(size));
        return body;
    }

    /**
     * Handles all RuntimeExceptions and returns a standardized error response.
     *
     * @param error The error
     * @return Standardized error response as a string
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleError(RuntimeException error) {
        log.error(error.getMessage(), error);
        String cause = error.getCause() != null ? error.getCause().getMessage() : error.getMessage();
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(cause);
    }
}
