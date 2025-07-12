package com.beassolution.rule.dto.response.base;

import lombok.Data;

/**
 * Generic base response model for API responses with data payload.
 * 
 * <p>This class provides a generic wrapper for API responses that include
 * both status information and a typed response payload. It extends the
 * BaseResponse to include a generic response object.
 * 
 * <p>Key components include:
 * <ul>
 *   <li>Status information from BaseResponse</li>
 *   <li>Generic response payload</li>
 * </ul>
 * 
 * @param <T> The type of the response payload
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Data
public class BaseResponseModel<T> {
    
    /**
     * Status information for the response.
     * 
     * <p>This field contains the status details including error code,
     * status message, and any informational messages.
     */
    private BaseResponse status = new BaseResponse();
    
    /**
     * The actual response payload.
     * 
     * <p>This field contains the typed response data that is specific
     * to the API endpoint being called.
     */
    private T response;
}
