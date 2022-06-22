package org.sample.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;


@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Getter @Setter
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


}
