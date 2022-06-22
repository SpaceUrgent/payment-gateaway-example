package org.sample.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@JsonIgnoreProperties
@ToString
@Builder
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
