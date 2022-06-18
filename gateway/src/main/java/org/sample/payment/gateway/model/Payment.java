package org.sample.payment.gateway.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor

@Getter @Setter
@Component
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)

public class Payment {

    @JsonProperty("invoice")
    @NotNull
    @Id
    private String invoice;

    @JsonProperty("amount")
    @NotNull
    private double amount;

    @JsonProperty("currency")
    @NotNull
    private String currency;

    @JsonProperty("cardholder")
    @NotNull
    private Cardholder cardholder;

    @JsonProperty("card")
    @NotNull
    private Card card;


}
