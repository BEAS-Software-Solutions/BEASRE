package com.beassolution.rule.exception;

import com.beassolution.rule.exception.base.BaseException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception class for operation-related errors in the Beas Rule Engine.
 * 
 * <p>This class represents exceptions that occur during business operations
 * such as rule execution, data processing, and system operations. It extends
 * BaseException to provide enhanced error handling capabilities.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>HTTP status code integration</li>
 *   <li>Error code management</li>
 *   <li>Stack trace filtering for JSON serialization</li>
 *   <li>Detailed error information</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class OperationException extends BaseException {

	/**
	 * Serial version UID for serialization.
	 */
	private static final long serialVersionUID = -6968062426465693238L;

    /**
     * Constructor that wraps an existing throwable.
     * 
     * @param e The throwable to wrap
     */
    public OperationException(Throwable e) {
        super(e);
    }

    /**
     * Constructor with message, HTTP status, and cause.
     * 
     * @param message The error message
     * @param status The HTTP status code
     * @param e The cause of the exception
     */
    public OperationException(String message, HttpStatus status, Throwable e) {
        super(e);
        setStatus(status);
        setMessage(message);
        setErrorCode(status.value());
        setReason(status.getReasonPhrase());
    }
    
    /**
     * Constructor with message and HTTP status.
     * 
     * @param message The error message
     * @param status The HTTP status code
     */
	public OperationException(String message, HttpStatus status) {
        setStatus(status);
        setMessage(message);
        setErrorCode(status.value());
        setReason(status.getReasonPhrase());
    }

    /**
     * Constructor with message only.
     * 
     * <p>This constructor creates an exception with the specified message
     * and sets the HTTP status to INTERNAL_SERVER_ERROR.
     * 
     * @param message The error message
     */
    public OperationException(String message) {
        setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        setReason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        setMessage(message);
    }

    /**
     * Overrides getStackTrace to exclude it from JSON serialization.
     * 
     * <p>This method is marked with @JsonIgnore to prevent stack trace
     * information from being included in JSON responses for security reasons.
     * 
     * @return The stack trace elements
     */
    @Override
    @JsonIgnore
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    /**
     * Gets detailed error information as a string.
     * 
     * <p>This method returns a string representation of the error details,
     * including the first line of the stack trace for debugging purposes.
     * 
     * @return String containing error details
     */
    public String getErrorDetail() {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            printStackTrace(pw);
            sw.flush();
            sw.close();
            return sw.toString().split("\\n\\tat")[0];
        } catch (IOException e) {
            log.error("ERR:", e);
            return "Error detail not serialized";
        }
    }
}