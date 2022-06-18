package org.sample.payment.gateway.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cardholders")
@Getter @Setter
public class CardholderRecord {

    @Column(name = "name")
    public String name;

    @Column(name = "email", unique = true)
    @Id
    public String email;

    @OneToMany
    private List<PaymentRecord> paymentRecords = new ArrayList<>();

//    public void addPaymentRecord(PaymentRecord paymentRecord){
//        paymentRecords.add(paymentRecord);
//        paymentRecord.setCardholderRecord(this);
//    }
//
//    public void removePaymentRecord(PaymentRecord paymentRecord){
//        paymentRecords.remove(paymentRecord);
//        paymentRecord.setCardholderRecord(null);
//    }
}
