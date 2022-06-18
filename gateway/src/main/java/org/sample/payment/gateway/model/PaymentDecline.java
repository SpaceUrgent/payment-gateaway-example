package org.sample.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PaymentDecline {
    @JsonProperty
    private final String approved = "false";
    @JsonProperty
    private Map<String, String> errors;

    public PaymentDecline(Map<String, String> errors) {
        this.errors = errors;
    }

    public String getApproved() {
        return approved;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
