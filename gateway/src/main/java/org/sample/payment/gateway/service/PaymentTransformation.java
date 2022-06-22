package org.sample.payment.gateway.service;

import org.sample.payment.gateway.model.Payment;
import org.sample.payment.gateway.model.PaymentRecord;
import org.sample.payment.gateway.model.PaymentResponse;

public interface PaymentTransformation {

    public PaymentRecord transformFromPaymentToPaymentRecord(Payment payment);
    public PaymentResponse transformPaymentRecordToResponse(PaymentRecord paymentRecord);

    public PaymentResponse transformPaymentToPaymentResponse(Payment payment);

}
