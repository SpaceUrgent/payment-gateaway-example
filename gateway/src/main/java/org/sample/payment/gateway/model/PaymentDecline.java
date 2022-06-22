package org.sample.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class PaymentDecline {
    @JsonProperty
    private final String approved = "false";
    @JsonProperty
    private Map<String, String> errors;

    public PaymentDecline(Map<String, String> errors) {
        this.errors = errors;
    }

}
