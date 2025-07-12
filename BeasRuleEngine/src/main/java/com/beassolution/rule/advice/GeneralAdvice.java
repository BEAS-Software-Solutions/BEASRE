package com.beassolution.rule.advice;

import com.beassolution.rule.dto.response.base.BaseResponse;
import com.beassolution.rule.dto.response.base.BaseResponseModel;
import com.beassolution.rule.exception.OperationException;
import org.springframework.core.MethodParameter;
import org.springframework.data.mongodb.core.MongoDataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collection;

/**
 * Global exception handler and response body advice for the Beas Rule Engine.
 * 
 * <p>This class provides centralized exception handling and response processing
 * for the entire application. It converts various exceptions into standardized
 * response formats and adds metadata to collection responses.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Global exception handling</li>
 *   <li>Standardized error responses</li>
 *   <li>Collection response metadata</li>
 *   <li>Exception type conversion</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@ControllerAdvice
public class GeneralAdvice implements ResponseBodyAdvice<Collection<?>> {

    /**
     * Converts a Throwable to an OperationException with appropriate status.
     * 
     * @param throwable The throwable to convert
     * @param status The HTTP status code
     * @return The converted OperationException
     */
    public static OperationException convertToOperationException(Throwable throwable, HttpStatus status) {
        var operationException = new OperationException(throwable.getMessage(), status);
        operationException.setStackTrace(throwable.getStackTrace());
        operationException.fillInStackTrace();
        operationException.setMessage(throwable.getMessage());
        operationException.setReason(throwable.getLocalizedMessage());
        return operationException;
    }

    /**
     * Generates a standardized error response model.
     * 
     * @param operationException The OperationException to convert
     * @return The standardized response model
     */
    public static BaseResponseModel<OperationException> generateOperationMessage(OperationException operationException) {
        var baseModel = new BaseResponseModel<OperationException>();
        var baseResponse = new BaseResponse();
        baseResponse.setMessage("FAIL");
        baseResponse.setStatus(operationException.getReason());
        baseResponse.setErrorCode(operationException.getErrorCode());
        baseModel.setStatus(baseResponse);
        baseModel.setResponse(operationException);
        return baseModel;
    }

    /**
     * Determines if the advice should be applied to the response.
     * 
     * @param returnType The method return type
     * @param converterType The message converter type
     * @return true if the response is a Collection
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Collection.class.isAssignableFrom(returnType.getParameterType());
    }

    /**
     * Adds metadata to collection responses.
     * 
     * <p>This method adds an X-Total-Count header to collection responses
     * to provide information about the total number of items.
     * 
     * @param body The response body
     * @param returnType The method return type
     * @param selectedContentType The selected content type
     * @param selectedConverterType The selected converter type
     * @param request The HTTP request
     * @param response The HTTP response
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
     * Handles MongoDB data integrity violation exceptions.
     * 
     * @param exception The MongoDB data integrity violation exception
     * @return Standardized error response
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(MongoDataIntegrityViolationException.class)
    public BaseResponseModel<OperationException> handleMongoDataIntegrityViolation(MongoDataIntegrityViolationException exception) {
        return generateOperationMessage(convertToOperationException(exception, HttpStatus.BAD_REQUEST));
    }

    /**
     * Handles OperationException instances.
     * 
     * @param exception The OperationException
     * @return Standardized error response
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OperationException.class)
    public BaseResponseModel<OperationException> handleOperationException(OperationException exception) {
        return generateOperationMessage(exception);
    }

    /**
     * Handles HTTP message conversion exceptions.
     * 
     * @param exception The HTTP message conversion exception
     * @return Standardized error response
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public BaseResponseModel<OperationException> handleHttpMessageConversionException(HttpMessageConversionException exception) {
        return generateOperationMessage(convertToOperationException(exception, HttpStatus.BAD_REQUEST));
    }

    /**
     * Handles HTTP request method not supported exceptions.
     * 
     * @param exception The HTTP request method not supported exception
     * @return Standardized error response
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponseModel<OperationException> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        return generateOperationMessage(convertToOperationException(exception, HttpStatus.METHOD_NOT_ALLOWED));
    }

    /**
     * Handles method argument not valid exceptions.
     * 
     * @param exception The method argument not valid exception
     * @return Standardized error response
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponseModel<OperationException> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return generateOperationMessage(convertToOperationException(exception, HttpStatus.BAD_REQUEST));
    }

    /**
     * Handles no handler found exceptions.
     * 
     * @param exception The no handler found exception
     * @return Standardized error response
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public BaseResponseModel<OperationException> handleNoHandlerFoundException(NoHandlerFoundException exception) {
        return generateOperationMessage(convertToOperationException(exception, HttpStatus.NOT_FOUND));
    }

    /**
     * Handles general throwable exceptions.
     * 
     * @param throwable The throwable exception
     * @return Standardized error response
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public BaseResponseModel<OperationException> handleThrowable(Throwable throwable) {
        return generateOperationMessage(convertToOperationException(throwable, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * Handles general exceptions.
     * 
     * @param exception The exception
     * @return Standardized error response
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseResponseModel<OperationException> handleException(Exception exception) {
        return generateOperationMessage(convertToOperationException(exception, HttpStatus.INTERNAL_SERVER_ERROR));
    }
    
    /**
     * Handles Error instances.
     * 
     * @param error The error
     * @return Standardized error response
     */
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Error.class)
    public BaseResponseModel<OperationException> handleError(Error error) {
        var cause = error.getCause() != null ? error.getCause() : error;
        return generateOperationMessage(convertToOperationException(cause, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
