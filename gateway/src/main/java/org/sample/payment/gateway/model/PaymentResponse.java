package org.sample.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter @Setter
@JsonIgnoreProperties
@ToString
public class PaymentResponse {

    @NotNull
    private String invoice;

    @NotNull
    private double amount;

    @NotNull
    private String currency;

    @NotNull
    private CardholderResponse cardholder;

    @NotNull
    private CardResponse card;


}
