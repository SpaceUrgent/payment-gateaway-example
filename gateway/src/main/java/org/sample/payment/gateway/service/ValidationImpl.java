package org.sample.payment.gateway.service;

import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;


import org.sample.payment.gateway.model.Card;
import org.sample.payment.gateway.model.Cardholder;
import org.sample.payment.gateway.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.sample.payment.gateway.constants.Constant.*;

@Service
public class ValidationImpl implements Validation{

    public PaymentRecordService paymentRecordService;

    @Autowired
    public ValidationImpl(PaymentRecordService paymentRecordService) {
        this.paymentRecordService = paymentRecordService;
    }


    @Override
    public Map<String, String> validatePayment(Payment payment){
        Map<String, String> errors = new HashMap<>();

        errors = validateInvoiceField(payment.getInvoice(), errors);
        errors = validateAmount(payment.getAmount(), errors);
        errors = validateCurrencyField(payment.getCurrency(), errors);
        errors = validateCardholder(payment.getCardholder(), errors);
        errors = validateCard(payment.getCard(), errors);

        return errors;
    }


    public Map<String, String> validateInvoiceField(String invoice, Map<String, String> errors){
        if (invoice.isEmpty()) errors.put(ERROR_KEY_INVOICE, "Invoice is required");
        if (checkPaymentsWithSameInvoice(invoice) == true) errors.put(ERROR_KEY_INVOICE, "Payment with same invoice exists. Multiple payments with same invoice can not be registered");
        return errors;
    }

    private boolean checkPaymentsWithSameInvoice(String invoice) {
        return paymentRecordService.checkIfInvoiceExists(invoice);
    }

    public Map<String, String> validateAmount(double amount, Map<String, String> errors){
        errors = validateAmountNotZero(amount, errors);
        errors = validateAmountNotNegative(amount, errors);
        return errors;
    }

    public Map<String, String> validateAmountNotZero(double amount, Map<String, String> errors){
        if (amount == 0) errors.put(ERROR_KEY_AMOUNT, "Amount is required");
        return errors;
    }

    public Map<String, String> validateAmountNotNegative(double amount, Map<String, String> errors){
        if (amount < 0) errors.put(ERROR_KEY_AMOUNT, "Amount is negative");
        return errors;
    }
    public Map<String, String> validateCurrencyField(String currency, Map<String, String> errors){
        errors = validateCurrencyFieldNotEmpty(currency, errors);
        if(!errors.containsKey(ERROR_KEY_CURRENCY)) errors = validateCurrencyFieldFormat(currency, errors);
        return errors;
    }
    public Map<String, String> validateCurrencyFieldNotEmpty(String currency, Map<String, String> errors){
        if (currency.isEmpty()) errors.put(ERROR_KEY_CURRENCY, "Currency is required");
        return errors;
    }

    public Map<String, String> validateCurrencyFieldFormat(String currency, Map<String, String> errors){
        if (!currency.matches(CURRENCY_REGEX)) errors.put(ERROR_KEY_CURRENCY, "Invalid currency format");
        return errors;
    }

    public Map<String, String> validateCardholder(Cardholder cardholder, Map<String, String> errors){
        if (cardholder == null) {
            errors.put(ERROR_KEY_CARDHOLDER, "Cardholder is required");
        } else {
            errors = validateCardholderName(cardholder.getName(), errors);
            errors = validateCardholderEmail(cardholder.getEmail(), errors);
        }
        return errors;
    }

    public Map<String, String> validateCardholderName(String name, Map<String, String> errors){
        errors = validateCardholderNameNotEmpty(name, errors);
        if(!errors.containsKey(ERROR_KEY_NAME)){ errors = validateCardholderNameFormat(name, errors);}
        return errors;
    }

    public Map<String, String> validateCardholderNameNotEmpty(String name, Map<String, String> errors){
        if (name.isEmpty()) errors.put(ERROR_KEY_NAME, "Cardholder name is required");
        return errors;
    }

    public Map<String, String> validateCardholderNameFormat(String name, Map<String, String> errors){
        if (!name.matches(CARDHOLDER_NAME_REGEX)) errors.put(ERROR_KEY_NAME, "Invalid cardholder name format");
        return errors;
    }
    public Map<String, String> validateCardholderEmail(String email, Map<String, String> errors){
        errors = validateCardholderEmailNotEmpty(email, errors);
        if(!errors.containsKey(ERROR_KEY_EMAIL)) errors = validateCardholderEmailFormat(email, errors);
        return errors;
    }

    public Map<String, String> validateCardholderEmailNotEmpty(String email, Map<String, String> errors){
        if (email.isEmpty()) errors.put(ERROR_KEY_EMAIL, "Email is required");
        return errors;
    }
    public Map<String, String> validateCardholderEmailFormat(String email, Map<String, String> errors){
        if (!email.matches(EMAIL_REGEX)) errors.put(ERROR_KEY_EMAIL, "Invalid cardholder email format");
        return errors;
    }

    public Map<String, String> validateCard(Card card, Map<String, String> errors){
        if (card == null) {
            errors.put(ERROR_KEY_CARD, "Card is required");
        } else {
            errors = validateCardPan(card.getPan(), errors);
            errors = validateCardExpiry(card.getExpiry(), errors);
            errors = validateCardCvv(card.getCvv(), errors);
        }

        return errors;
    }

    public Map<String, String> validateCardPan(String pan, Map<String, String> errors){
        errors = validateCardPanNotEmpty(pan, errors);
        if(!errors.containsKey(ERROR_KEY_PAN)){ errors = validateCardPanFormat(pan, errors);}
        if (!errors.containsKey(ERROR_KEY_PAN)){ errors = luhnCheck(pan, errors);}
        return errors;
    }

    public Map<String, String> validateCardPanNotEmpty(String pan, Map<String, String> errors){
        if (pan.isEmpty()) errors.put(ERROR_KEY_PAN, "Pan is required");
        return errors;
    }
    public Map<String, String> validateCardPanFormat(String pan, Map<String, String> errors){
        if (!pan.matches(PAN_REGEX)) errors.put(ERROR_KEY_PAN, "Invalid card pan format");
        return errors;
    }

    public Map<String, String> luhnCheck(String pan, Map<String, String> errors){
       if (!LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(pan)) errors.put(ERROR_KEY_PAN, "Invalid pan number");
       return errors;
    }

    public Map<String, String> validateCardExpiry(String expiry, Map<String, String> errors){
        errors = validateCardExpiryNotEmpty(expiry, errors);
        if(!errors.containsKey(ERROR_KEY_CARD)){ errors = validateCardExpiryFormat(expiry, errors);}
        if(!errors.containsKey(ERROR_KEY_CARD)){ errors = checkExpiryOverdue(expiry, errors);}

        return errors;
    }

    public Map<String, String> validateCardExpiryNotEmpty(String expiry, Map<String, String> errors){
        if (expiry.isEmpty()) errors.put(ERROR_KEY_EXPIRY, "Expiry is required");
        return errors;
    }

    public Map<String, String> validateCardExpiryFormat(String expiry, Map<String, String> errors){
        if (!expiry.matches(EXPIRY_REGEX)) errors.put(ERROR_KEY_EXPIRY, "Invalid card expiry format");
        return errors;
    }

    public Map<String, String> checkExpiryOverdue(String expiry, Map<String, String> errors){
        int month = Integer.parseInt(expiry.substring(0, 2));
        int year = Integer.parseInt("20" + expiry.substring(2, 4));
        LocalDate expiryDate = LocalDate.of(year, month, 1);
        LocalDate currentDate = LocalDate.now();

        if (expiryDate.isBefore(currentDate)) errors.put(ERROR_KEY_EXPIRY, "Card is expired");
        return errors;
    }

    public Map<String, String> validateCardCvv(String cvv, Map<String, String> errors){

        errors = validateCardCvvNotEmpty(cvv, errors);
        if(!errors.containsKey(ERROR_KEY_CVV)) {
            errors = validateCardCvvFormat(cvv, errors);
        }
        return errors;
    }

    public Map<String, String> validateCardCvvNotEmpty(String cvv, Map<String, String> errors){
        if (cvv.isEmpty()) errors.put(ERROR_KEY_CVV, "CVV is required");
        return errors;
    }
    public Map<String, String> validateCardCvvFormat(String cvv, Map<String, String> errors){
        if (!cvv.matches(CVV_REGEX)) errors.put(ERROR_KEY_CVV, "Invalid card cvv format");
        return errors;
    }


}
