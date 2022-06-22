package org.sample.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Getter @Setter
public class Cardholder {

    @JsonProperty("name")
    @NotNull
    private String name;

    @JsonProperty("email")
    @NotNull
    private String email;


}
