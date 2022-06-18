package org.sample.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PaymentConfirmation {

    @JsonProperty
    private final String approved = "true";

}
