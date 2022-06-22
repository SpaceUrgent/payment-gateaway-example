package org.sample.payment.gateway.service;

import lombok.Getter;
import org.sample.payment.gateway.model.PaymentRecord;
import org.sample.payment.gateway.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Service
public class PaymentRecordService {

    private static PaymentRepository paymentRepository;


    @Autowired
    public PaymentRecordService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    public void savePaymentRecord(PaymentRecord paymentRecord){
        paymentRepository.save(paymentRecord);

    }

    public PaymentRecord getPayment(String invoice){
        Optional<PaymentRecord> paymentRecordOptional = paymentRepository.findById(invoice);
        return paymentRecordOptional.get();
    }

    public boolean checkIfInvoiceExists(String invoice){
        return paymentRepository.existsById(invoice);
    }
}
