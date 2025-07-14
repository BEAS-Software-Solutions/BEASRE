package com.beassolution.ruleengine.model.response;

import com.beassolution.ruleengine.model.response.base.BaseResponseModel;
import lombok.Data;

/**
 * Response model for rule evaluation results from the BEAS Rule Engine.
 * <p>
 * Extends {@link BaseResponseModel} to include the evaluated response payload.
 *
 * @author BEAS Solution Team
 * @since 1.0
 */
@Data
public class RuleEvaluateResponse extends BaseResponseModel<Object> {

    /**
     * Gets the evaluated response payload.
     * @return the response object
     */
    public Object getResponse() {
        return super.getResponse();
    }
}
