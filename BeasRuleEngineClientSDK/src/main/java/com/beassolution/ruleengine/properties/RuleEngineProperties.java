package com.beassolution.ruleengine.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "rule.engine")
public class RuleEngineProperties {

    private String baseUrl;
    private boolean enabled = true;
    private boolean useClientToken = false;

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isUseClientToken() {
        return useClientToken;
    }
}
