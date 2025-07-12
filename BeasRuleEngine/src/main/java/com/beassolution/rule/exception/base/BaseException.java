package com.beassolution.rule.exception.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Base exception class for the Beas Rule Engine.
 * 
 * <p>This class provides a foundation for all custom exceptions in the
 * Beas Rule Engine. It includes common fields for error tracking and
 * provides JSON serialization support for API responses.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Timestamp tracking</li>
 *   <li>Error code management</li>
 *   <li>HTTP status integration</li>
 *   <li>JSON serialization support</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseException extends RuntimeException {
    
    /**
     * Timestamp when the exception occurred.
     * 
     * <p>This field is automatically set when the exception is created
     * and is formatted as a string for JSON serialization.
     */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	
	/**
	 * Numeric error code for the exception.
	 * 
	 * <p>This field contains a numeric code that can be used to identify
	 * the specific type of error that occurred.
	 */
	private int errorCode;
	
	/**
	 * HTTP status code associated with the exception.
	 * 
	 * <p>This field contains the appropriate HTTP status code that should
	 * be returned when this exception occurs in a REST API context.
	 */
	private HttpStatus status;
	
	/**
	 * Human-readable error message.
	 * 
	 * <p>This field contains a descriptive message explaining what went wrong.
	 */
	private String message;
	
	/**
	 * Reason for the exception.
	 * 
	 * <p>This field provides additional context about why the exception occurred.
	 */
	private String reason;

	/**
	 * Default constructor that initializes the timestamp.
	 * 
	 * <p>This constructor creates a new exception and sets the timestamp
	 * to the current date and time.
	 */
	public BaseException() {
		timestamp = LocalDateTime.now();
	}

	/**
	 * Constructor that wraps an existing throwable.
	 * 
	 * @param e The throwable to wrap
	 */
	public BaseException(Throwable e) {
		super(e);
	}
}