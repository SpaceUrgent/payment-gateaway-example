package org.sample.payment.gateway.service;

import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

import org.sample.payment.gateway.exception.CustomValidationException;
import org.sample.payment.gateway.model.Card;
import org.sample.payment.gateway.model.Cardholder;
import org.sample.payment.gateway.model.Payment;
import org.sample.payment.gateway.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.sample.payment.gateway.constants.Constant.*;

@Service
public class ValidationImpl implements Validation{

//    private ValidationImpl validation;


    public PaymentRecordService paymentRecordService;

    @Autowired
    public ValidationImpl(PaymentRecordService paymentRecordService) {
        this.paymentRecordService = paymentRecordService;
    }

    private Map<String, String> errors;

    @Override
    public Map<String, String> validatePayment(Payment payment) throws CustomValidationException {
        errors = new HashMap<>();
        try {
            validateInvoiceField(payment.getInvoice());
        } catch (CustomValidationException exception) {
            String invalidField = "invoice:";
            String error = exception.getMessage();
            errors.put(invalidField, error);
        }
        validateAmount(payment.getAmount());
        validateCurrencyField(payment.getCurrency());
        try {
            validateCardholder(payment.getCardholder());
        } catch (CustomValidationException e){
            errors.put("cardholder:", e.getMessage());
        }
        try {
            validateCard(payment.getCard());
        } catch (CustomValidationException e){
            errors.put("card:", e.getMessage());
        }
        return errors;
    }


    public void validateInvoiceField(String invoice) throws CustomValidationException {
        if (invoice.isEmpty()) throw new CustomValidationException("Invoice is required");
        if (checkPaymentsWithSameInvoice(invoice) == true)
            throw new CustomValidationException("Payment with same invoice exists. Multiple payments with same invoice can not be registered");
    }

    private boolean checkPaymentsWithSameInvoice(String invoice) {
        return paymentRecordService.checkIfInvoiceExists(invoice);
    }

    public void validateAmount(double amount) throws CustomValidationException{
        try {
            validateAmountNotZero(amount);
        } catch (CustomValidationException e){
            errors.put("amount:", e.getMessage());
        }
        try {
            validateAmountNotNegative(amount);
        } catch (CustomValidationException e){
            errors.put("amount:", e.getMessage());
        }
    }

    public void validateAmountNotZero(double amount) throws CustomValidationException{
        if (amount == 0) throw new CustomValidationException("Amount is required");
    }

    public void validateAmountNotNegative(double amount) throws CustomValidationException{
        if (amount < 0) throw new CustomValidationException("Amount is negative");
    }
    public void validateCurrencyField(String currency) throws CustomValidationException {
        try {
            validateCurrencyFieldNotEmpty(currency);
        } catch (CustomValidationException e){
            errors.put("currency:", e.getMessage());
        }
        try {
            validateCurrencyFieldFormat(currency);
        } catch (CustomValidationException e){
            errors.put("currency:", e.getMessage());
        }

    }
    public void validateCurrencyFieldNotEmpty(String currency) throws CustomValidationException {
        if (currency.isEmpty()) throw new CustomValidationException("Currency is required");
    }

    public void validateCurrencyFieldFormat(String currency) throws CustomValidationException {
        if (!currency.matches(CURRENCY_REGEX)) throw new CustomValidationException("Invalid currency format");
    }

    public void validateCardholder(Cardholder cardholder) throws CustomValidationException {
        if (cardholder == null) {
            throw new CustomValidationException("Cardholder is required");
        } else {
            validateCardholderName(cardholder.getName());
            validateCardholderEmail(cardholder.getEmail());
        }
    }

    public void validateCardholderName(String name) throws CustomValidationException {
        try {
            validateCardholderNameNotEmpty(name);
            validateCardholderNameFormat(name);
        } catch (CustomValidationException e){
            errors.put("name:",e.getMessage());
        }
    }

    public void validateCardholderNameNotEmpty(String name) throws CustomValidationException {
        if (name.isEmpty()) throw new CustomValidationException("Cardholder name is required");
    }

    public void validateCardholderNameFormat(String name) throws CustomValidationException {
        if (!name.matches(CARDHOLDER_NAME_REGEX)) throw new CustomValidationException("Invalid cardholder name format");
    }
    public void validateCardholderEmail(String email) throws CustomValidationException {
        try {
            validateCardholderEmailNotEmpty(email);
            validateCardholderEmailFormat(email);
        } catch (CustomValidationException e){
            errors.put("email:", e.getMessage());
        }
    }

    public void validateCardholderEmailNotEmpty(String email) throws CustomValidationException {
        if (email.isEmpty()) throw new CustomValidationException("Email is required");
    }
    public void validateCardholderEmailFormat(String email) throws CustomValidationException {
        if (!email.matches(EMAIL_REGEX)) throw new CustomValidationException("Invalid cardholder email format");
    }

    private void validateCard(Card card) throws CustomValidationException {
        if (card == null) {
            throw new CustomValidationException("Card is required");
        } else {
            validateCardPan(card.getPan());
            validateCardExpiry(card.getExpiry());
            validateCardCvv(card.getCvv());
        }

    }

    public void validateCardPan(String pan) throws CustomValidationException {
        try {
            validateCardPanNotEmpty(pan);
            validateCardPanFormat(pan);
            luhnCheck(pan);
        } catch (CustomValidationException e){
            errors.put("pan:", e.getMessage());
        }
    }

    public void validateCardPanNotEmpty(String pan) throws CustomValidationException {
        if (pan.isEmpty()) throw new CustomValidationException("Pan is required");
    }
    public void validateCardPanFormat(String pan) throws CustomValidationException {

        if (!pan.matches(PAN_REGEX)) throw new CustomValidationException("Invalid card pan format");

    }

    public void luhnCheck(String pan) throws CustomValidationException {
       if (!LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(pan)) throw new CustomValidationException("Invalid pan number");
    }

    public void validateCardExpiry(String expiry) throws CustomValidationException {
        try {
            validateCardExpiryNotEmpty(expiry);
            validateCardExpiryFormat(expiry);
            checkExpiryOverdue(expiry);
        } catch (CustomValidationException e){
            errors.put("expiry:", e.getMessage());
        }
    }

    public void validateCardExpiryNotEmpty(String expiry) throws CustomValidationException {
        if (expiry.isEmpty()) throw new CustomValidationException("Expiry is required");
    }

    public void validateCardExpiryFormat(String expiry) throws CustomValidationException {
        if (!expiry.matches(EXPIRY_REGEX)) throw new CustomValidationException("Invalid card expiry format");
    }

    public void checkExpiryOverdue(String expiry) throws CustomValidationException {
        int month = Integer.parseInt(expiry.substring(0, 2));
        int year = Integer.parseInt("20" + expiry.substring(2, 4));
        LocalDate expiryDate = LocalDate.of(year, month, 1);
        LocalDate currentDate = LocalDate.now();

        if (expiryDate.isBefore(currentDate)) throw new CustomValidationException("Card is expired");
    }

    public void validateCardCvv(String cvv) throws CustomValidationException {
        try {
            validateCardCvvNotEmpty(cvv);
            validateCardCvvFormat(cvv);
        } catch (CustomValidationException e){
            errors.put("cvv:", e.getMessage());
        }
    }

    public void validateCardCvvNotEmpty(String cvv) throws CustomValidationException {
        if (cvv.isEmpty()) throw new CustomValidationException("CVV is required");
    }
    public void validateCardCvvFormat(String cvv) throws CustomValidationException {
        if (!cvv.matches(CVV_REGEX)) throw new CustomValidationException("Invalid card cvv format");
    }


}
