package org.sample.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.Entity;


@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder

public class Card {

    @JsonProperty("pan")
    @NotNull
    private String pan;

    @JsonProperty("expiry")
    @NotNull
    private String expiry;

    @JsonProperty("cvv")
    @NotNull
    private String cvv;


    public String getPan() {
        return pan;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getCvv() {
        return cvv;
    }
}
