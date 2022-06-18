package org.sample.payment.gateway.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "payments")
@Getter @Setter
@JsonIgnoreProperties
public class PaymentRecord {

    @Id
    @NotNull
    @Column(name = "invoice_id")
    private String invoice;

    @NotNull
    @Column(name = "amount")
    private double amount;

    @NotNull
    @Column(name = "currency")
    private String currency;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "cardholder_id")
    private CardholderRecord cardholderRecord ;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    private CardRecord card;

}
