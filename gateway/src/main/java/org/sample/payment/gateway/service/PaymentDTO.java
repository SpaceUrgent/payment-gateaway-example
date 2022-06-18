package org.sample.payment.gateway.service;

import org.sample.payment.gateway.model.Payment;
import org.sample.payment.gateway.model.PaymentRecord;
import org.sample.payment.gateway.model.PaymentResponse;

public interface PaymentDTO {

    public PaymentRecord transposeFromPaymentToPaymentRecord(Payment payment);
    public PaymentResponse transposePaymentRecordToResponse(PaymentRecord paymentRecord);

    public PaymentResponse transposePaymentToPaymentResponse(Payment payment);

}
