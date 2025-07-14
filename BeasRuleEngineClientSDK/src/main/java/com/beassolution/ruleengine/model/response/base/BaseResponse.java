package com.beassolution.ruleengine.model.response.base;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Standardized base response for all API responses in the BEAS Rule Engine.
 * <p>
 * Includes error code, status, message, and additional info list.
 *
 * @author BEAS Solution Team
 * @since 1.0
 */
@Data
public class BaseResponse {

    /**
     * Error code indicating the response status (0 = success).
     */
    private int errorCode;
    /**
     * Status message (e.g., "Success" or "Error").
     */
    private String status;
    /**
     * Detailed message about the operation result.
     */
    private String message;
    /**
     * List of additional informational messages.
     */
    private List<String> infoList;

    /**
     * Default constructor initializing a successful response.
     */
    public BaseResponse() {
        infoList = new LinkedList<>();
        this.errorCode = 0;
        status = "Success";
        message = "Successfully";
    }

    /**
     * Constructor with custom status and message.
     * @param status the status message
     * @param message the detailed message
     */
    public BaseResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.errorCode = 0;
    }

    /**
     * Full constructor with all fields.
     * @param errorCode the error code
     * @param status the status message
     * @param message the detailed message
     * @param infoList list of informational messages
     */
    public BaseResponse(int errorCode, String status, String message, List<String> infoList) {
        this.errorCode = errorCode;
        this.status = status;
        this.message = message;
        this.infoList = infoList;
    }
}

