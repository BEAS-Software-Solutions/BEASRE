package com.beassolution.ruleengine.model.response.base;

import lombok.Data;

/**
 * Generic base response model for API responses with data payload.
 * <p>
 * Provides a wrapper for API responses that include both status information and a typed response payload.
 *
 * @param <T> The type of the response payload
 *
 * @author BEAS Solution Team
 * @since 1.0
 */
@Data
public class BaseResponseModel<T> {
    /**
     * Status information for the response, including error code and messages.
     */
    private BaseResponse status = new BaseResponse();
    /**
     * The actual response payload.
     */
    private T response;

    /**
     * Gets the response payload.
     * @return the response object
     */
    public T getResponse() {
        return response;
    }
}
