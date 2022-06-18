package org.sample.payment.gateway;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sample.payment.gateway.exception.CustomValidationException;
import org.sample.payment.gateway.model.*;
import org.sample.payment.gateway.service.ValidationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class ValidationTest {

    @Autowired
    private static ValidationImpl validation;

    private static List<Payment> payments;

    @BeforeAll
    public static void setup(){
//         validation = new ValidationImpl();
         payments = new ArrayList<>();

         Card validCard = Card.builder()
                .pan("4200000000000001")
                .expiry("0624")
                .cvv("789")
                .build();

        Cardholder validCardholder = Cardholder.builder()
                .name("First Last")
                .email("email@domain.com")
                .build();

        Payment validPayment = Payment.builder()
                .invoice("1234567")
                .amount(1299)
                .currency("EUR")
                .cardholder(validCardholder)
                .card(validCard)
                .build();


        payments.add(0, validPayment);


    }

    @Test
    public void emptyInvoiceThrowException() throws ValidationException {
        String invoice = "";

        Exception exception = Assertions.assertThrows(CustomValidationException.class, () -> validation.validateInvoiceField(invoice));
        Assertions.assertEquals("Invoice is required", exception.getMessage());
    }

    @Test
    public void validPaymentInvoiceTest(){
        String invoice = "1234567";

        Assertions.assertDoesNotThrow(() -> validation.validateInvoiceField(invoice));
    }

    @Test
    public void validatePaymentAmountTest(){

        Assertions.assertDoesNotThrow(() -> validation.validateAmount(payments.get(0).getAmount()));

    }

    @Test
    public void validateZeroPaymentAmountThrowsException(){
        double amount = 0;
        Exception exception = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateAmountNotZero(amount));
        Assertions.assertEquals("Amount is required", exception.getMessage());
    }

    @Test
    public void validateNegativePaymentAmountThrowsException(){
        double amount = -100;
        Exception exception = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateAmountNotNegative(amount));

        Assertions.assertEquals("Amount is negative", exception.getMessage());
    }

    @Test
    public void validateCorrectPaymentCurrencyTest(){
        String currency = "USD";
        Assertions.assertDoesNotThrow(() -> validation.validateCurrencyField(currency));
    }

    @Test
    public void validateEmptyPaymentCurrencyTest(){
        String currency = "";
        Exception exception = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCurrencyFieldNotEmpty(currency));
        Assertions.assertEquals("Currency is required", exception.getMessage());
    }

    @Test
    public void validateIncorrectFormatPaymentCurrencyTest(){
        String currency1 = "123";
        String currency2 = "sss";

        Exception exception1 = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCurrencyFieldFormat(currency1));
        Exception exception2 = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCurrencyFieldFormat(currency2));
        Assertions.assertEquals("Invalid currency format", exception1.getMessage());
        Assertions.assertEquals("Invalid currency format", exception2.getMessage());

    }

    @Test
    public void validateCorrectCardholderNameTest(){
        String name = "Steve Carrell";

        Assertions.assertDoesNotThrow(() -> validation.validateCardholderName(name));

    }

    @Test
    public void validateEmptyCardholderNameTest(){
        String name = "";

        Exception exception = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardholderNameNotEmpty(name));
        Assertions.assertEquals("Cardholder name is required", exception.getMessage());

    }

    @Test
    public void validateIncorrectFormatCardholderNameTest(){
        String name1 = "123";
        String name2 = "!qwee -1";


        Exception exception1 = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardholderNameFormat(name1));
        Exception exception2 = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardholderNameFormat(name2));

        Assertions.assertEquals("Invalid cardholder name format", exception1.getMessage());
        Assertions.assertEquals("Invalid cardholder name format", exception2.getMessage());
    }

    @Test
    public void validateCorrectCardholderEmailTest(){
        String email = "email@domain.com";

        Assertions.assertDoesNotThrow(() -> validation.validateCardholderEmailFormat(email));
    }


    @Test
    public void validateEmptyCardholderEmailThrowsExceptionTest(){
        String email = "";

        Exception exception = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardholderEmailNotEmpty(email));

        Assertions.assertEquals("Email is required", exception.getMessage());
    }

    @Test
    public void validateIncorrectFormatCardholderEmailThrowsExceptionTest(){
        String email1 = "12345";
        String email2 = "@asdafs1213";
        String email3 = "QWERTY";

        Exception exception1 = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardholderEmailFormat(email1));
        Exception exception2 = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardholderEmailFormat(email2));
        Exception exception3 = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardholderEmailFormat(email3));

        Assertions.assertEquals("Invalid cardholder email format", exception1.getMessage());
        Assertions.assertEquals("Invalid cardholder email format", exception2.getMessage());
        Assertions.assertEquals("Invalid cardholder email format", exception3.getMessage());
    }

    @Test
    public void validateCorrectCardPanTest(){
        String pan = "5277029120773860";

        Assertions.assertDoesNotThrow(() -> validation.validateCardPan(pan));
    }

    @Test
    public void validateEmptyCardPanThrowsExceptionTest(){
        String pan = "";

        Exception exception = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardPanNotEmpty(pan));

        Assertions.assertEquals("Pan is required", exception.getMessage());
    }

    @Test
    public void validateIncorrectFormatCardPanThrowsExceptionTest(){
        String pan1 = "1234567";

        String pan2 = "asdasfregeewr";


        Exception exception1 = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardPanFormat(pan1));
        Exception exception2 = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardPanFormat(pan2));

        Assertions.assertEquals("Invalid card pan format", exception1.getMessage());
        Assertions.assertEquals("Invalid card pan format", exception2.getMessage());
    }

    @Test
    public void validateIncorrectCardPanThrowsExceptionTest(){
        String pan = "4700000000000001";

        Exception exception = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.luhnCheck(pan));

        Assertions.assertEquals("Invalid pan number", exception.getMessage());

    }

    @Test
    public void validateCorrectExpiryDate(){
        String expiry = "1224";

        Assertions.assertDoesNotThrow(() -> validation.validateCardExpiry(expiry));
    }

    @Test
    public void checkEmptyExpiredDateThrowsException(){
        String expiry = "";

        Exception exception = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardExpiryNotEmpty(expiry));

        Assertions.assertEquals("Expiry is required", exception.getMessage());
    }

    @Test
    public void checkFormatExpiredDateThrowsException(){
        String expiry1 = "asd";
        String expiry2 = "1";

        Exception exception1 = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardExpiryFormat(expiry1));
        Exception exception2 = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.validateCardExpiryFormat(expiry2));


        Assertions.assertEquals("Invalid card expiry format", exception1.getMessage());
        Assertions.assertEquals("Invalid card expiry format", exception2.getMessage());
    }


    @Test
    public void checkExpiredDateThrowsException(){
        String expiry = "1219";

        Exception exception = Assertions.assertThrows(CustomValidationException.class,
                () -> validation.checkExpiryOverdue(expiry));

        Assertions.assertEquals("Card is expired", exception.getMessage());
    }

    @Test
    public void checkValidExpiryDate(){
        Card card = Card.builder()
                .pan("3543693387314139")
                .expiry("1224")
                .cvv("222")
                .build();
        String expiry = card.getExpiry();

        Assertions.assertDoesNotThrow(() -> validation.checkExpiryOverdue(expiry));

    }



}
