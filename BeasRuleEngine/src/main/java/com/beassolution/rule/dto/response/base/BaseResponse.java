package com.beassolution.rule.dto.response.base;


import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Base response class for all API responses in the Beas Rule Engine.
 * 
 * <p>This class provides a standardized response structure for all API endpoints.
 * It includes error handling, status information, and optional informational messages.
 * 
 * <p>Key components include:
 * <ul>
 *   <li>Error code for status identification</li>
 *   <li>Status message</li>
 *   <li>Detailed message</li>
 *   <li>List of informational messages</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Data
public class BaseResponse {

    /**
     * Error code indicating the response status.
     * 
     * <p>This field contains a numeric code that indicates the success or failure
     * of the operation. A value of 0 typically indicates success.
     */
    private int errorCode;
    
    /**
     * Status message describing the response.
     * 
     * <p>This field contains a brief status message such as "Success" or "Error"
     * that describes the overall result of the operation.
     */
    private String status;
    
    /**
     * Detailed message about the operation result.
     * 
     * <p>This field contains a more detailed description of what happened
     * during the operation, including success messages or error details.
     */
    private String message;
    
    /**
     * List of additional informational messages.
     * 
     * <p>This field contains a list of additional messages that provide
     * context or details about the operation result.
     */
    private List<String> infoList;

    /**
     * Default constructor that initializes a successful response.
     * 
     * <p>This constructor creates a response with default success values:
     * error code 0, status "Success", and message "Successfully".
     */
    public BaseResponse() {
        infoList = new LinkedList<>();
        this.errorCode = 0;
        status = "Success";
        message = "Successfully";
    }

    /**
     * Constructor with custom status and message.
     * 
     * @param status The status message
     * @param message The detailed message
     */
    public BaseResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.errorCode = 0;
    }

    /**
     * Full constructor with all fields.
     * 
     * @param errorCode The error code
     * @param status The status message
     * @param message The detailed message
     * @param infoList List of informational messages
     */
    public BaseResponse(int errorCode, String status, String message, List<String> infoList) {
        this.errorCode = errorCode;
        this.status = status;
        this.message = message;
        this.infoList = infoList;
    }


}

