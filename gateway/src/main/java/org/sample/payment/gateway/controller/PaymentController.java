package org.sample.payment.gateway.controller;

import org.sample.payment.gateway.exception.CustomValidationException;
import org.sample.payment.gateway.model.*;
import org.sample.payment.gateway.service.PaymentRecordService;
import org.sample.payment.gateway.service.PaymentDTO;
import org.sample.payment.gateway.service.ValidationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static org.sample.payment.gateway.logger.LoggerHelper.LOGGER;


@RestController
public class PaymentController {

    public PaymentRecordService paymentRecordService;
    public PaymentDTO paymentDTO;

    public ValidationImpl validation;

    @Autowired
    public PaymentController(PaymentRecordService paymentRecordService, PaymentDTO paymentToPaymentRecord, ValidationImpl validation) {
        this.paymentRecordService = paymentRecordService;
        this.paymentDTO = paymentToPaymentRecord;
        this.validation = validation;
    }

    @GetMapping(value = "/payment/get", produces = "application/json")
    public PaymentResponse getPayment(@RequestBody String invoice){
        PaymentRecord paymentRecord = paymentRecordService.getPayment(invoice);
        PaymentResponse paymentResponse = paymentDTO.transposePaymentRecordToResponse(paymentRecord);
        return paymentResponse;
    }



    @PostMapping(value = "/payment", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> requestPayment(@RequestBody Payment payment) throws CustomValidationException {
        ResponseEntity<Object> response = null;

        Map<String, String> errors = validation.validatePayment(payment);
        if (errors.isEmpty()) {
            response = new ResponseEntity<>(new PaymentConfirmation(), HttpStatus.ACCEPTED);
            PaymentRecord paymentRecord = paymentDTO.transposeFromPaymentToPaymentRecord(payment);
            paymentRecordService.savePaymentRecord(paymentRecord);
            LOGGER.info(paymentDTO.transposePaymentToPaymentResponse(payment).toString());
        } else {
            PaymentDecline decline = new PaymentDecline(errors);
            response = new ResponseEntity<>(decline, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

}
