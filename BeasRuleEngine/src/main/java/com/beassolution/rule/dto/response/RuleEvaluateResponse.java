package com.beassolution.rule.dto.response;

import com.beassolution.rule.dto.response.base.BaseResponseModel;
import lombok.Data;

/**
 * Response DTO for rule evaluation operations.
 * 
 * <p>This class represents the response payload for rule evaluation operations
 * in the Beas Rule Engine. It extends BaseResponseModel to provide a standardized
 * response structure with the rule execution result.
 * 
 * <p>The response contains the result of the rule execution, which can be
 * any type of object depending on the rule logic.
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Data
public class RuleEvaluateResponse extends BaseResponseModel<Object> {

}
