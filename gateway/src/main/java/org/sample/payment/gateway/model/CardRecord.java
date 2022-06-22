package org.sample.payment.gateway.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor

@Entity
@Table(name = "cards")
@Getter @Setter
@ToString
public class CardRecord {

    @Column(name = "pan", unique = true)
    @Id
    private String pan;

    @Column(name = "expiry")
    private String expiry;

    @OneToMany
    private List<PaymentRecord> paymentRecords = new ArrayList<>();


}
