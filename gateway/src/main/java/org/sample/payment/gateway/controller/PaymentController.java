package org.sample.payment.gateway.controller;

import org.sample.payment.gateway.exception.CustomValidationException;
import org.sample.payment.gateway.model.*;
import org.sample.payment.gateway.service.PaymentRecordService;
import org.sample.payment.gateway.service.PaymentTransformation;
import org.sample.payment.gateway.service.ValidationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.sample.payment.gateway.logger.LoggerHelper.LOGGER;


@RestController
public class PaymentController {

    public PaymentRecordService paymentRecordService;
    public PaymentTransformation paymentTransformation;

    public ValidationImpl validation;

    @Autowired
    public PaymentController(PaymentRecordService paymentRecordService, PaymentTransformation paymentToPaymentRecord, ValidationImpl validation) {
        this.paymentRecordService = paymentRecordService;
        this.paymentTransformation = paymentToPaymentRecord;
        this.validation = validation;
    }

    @GetMapping(value = "/payment/get", produces = "application/json")
    public PaymentResponse getPayment(@RequestBody String invoice){
        PaymentRecord paymentRecord = paymentRecordService.getPayment(invoice);
        PaymentResponse paymentResponse = paymentTransformation.transformPaymentRecordToResponse(paymentRecord);
        return paymentResponse;
    }



    @PostMapping(value = "/payment", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> requestPayment(@RequestBody Payment payment) throws CustomValidationException {
        ResponseEntity<Object> response = null;

        Map<String, String> errors = validation.validatePayment(payment);
        if (errors.isEmpty()) {
            response = new ResponseEntity<>(new PaymentConfirmation(), HttpStatus.ACCEPTED);
            PaymentRecord paymentRecord = paymentTransformation.transformFromPaymentToPaymentRecord(payment);
            paymentRecordService.savePaymentRecord(paymentRecord);
            LOGGER.info(paymentTransformation.transformPaymentToPaymentResponse(payment).toString());
        } else {
            PaymentDecline decline = new PaymentDecline(errors);
            response = new ResponseEntity<>(decline, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

}
