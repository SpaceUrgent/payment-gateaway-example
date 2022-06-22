package org.sample.payment.gateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.sample.payment.gateway.model.*;
import org.sample.payment.gateway.service.ValidationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.sample.payment.gateway.constants.Constant.*;

@SpringBootTest
@Controller
public class ValidationTest {

    @Autowired
    private ValidationImpl validation;

    private static List<Payment> payments = new ArrayList<>();

    @BeforeAll
    public static void setup(){


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
    public void emptyInvoicePutsError(){
        String invoice = "";
        Map<String, String> errors = new HashMap<>();

            errors = validation.validateInvoiceField(invoice, errors);

            boolean containsKey = errors.containsKey(ERROR_KEY_INVOICE);
            String errorValue = errors.get(ERROR_KEY_INVOICE);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("Invoice is required", errorValue);
    }

//    @Test
//    public void checkPaymentWithExistingInvoicePutError(){
//    }

    @Test
    public void validPaymentInvoiceTest(){
        String invoice = "1234567";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateInvoiceField(invoice, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_INVOICE);
        String errorValue = errors.get(ERROR_KEY_INVOICE);

        Assertions.assertFalse(containsKey);
        Assertions.assertNull(errorValue);
    }

    @Test
    public void zeroAmountValidationPutsError(){
        double amount = 0;
        Map<String, String> errors = new HashMap<>();

        errors = validation.validateAmount(amount, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_AMOUNT);
        String errorValue = errors.get(ERROR_KEY_AMOUNT);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("Amount is required" ,errorValue);
    }

    @Test
    public void negativeAmountValidationPutsError(){
        double amount = -100;
        Map<String, String> errors = new HashMap<>();

        errors = validation.validateAmount(amount, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_AMOUNT);
        String errorValue = errors.get(ERROR_KEY_AMOUNT);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("Amount is negative" ,errorValue);
    }

    @Test
    public void validAmountValidationTest(){
        double amount = 100;
        Map<String, String> errors = new HashMap<>();

        errors = validation.validateAmount(amount, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_AMOUNT);
        String errorValue = errors.get(ERROR_KEY_AMOUNT);

        Assertions.assertFalse(containsKey);
        Assertions.assertNull(errorValue);
    }

    @Test
    public void emptyCurrencyValidationPutsError(){
        String currency = "";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateCurrencyField(currency, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_CURRENCY);
        String errorValue = errors.get(ERROR_KEY_CURRENCY);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("Currency is required", errorValue);
    }

    @Test
    public void invalidCurrencyFormatValidationPutsError(){
        String currency = "111";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateCurrencyField(currency, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_CURRENCY);
        String errorValue = errors.get(ERROR_KEY_CURRENCY);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("Invalid currency format", errorValue);
    }

    @Test
    public void validCurrencyValidationTest(){
        String currency = "USD";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateCurrencyField(currency, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_CURRENCY);
        String errorValue = errors.get(ERROR_KEY_CURRENCY);

        Assertions.assertFalse(containsKey);
        Assertions.assertNull(errorValue);
    }

    @Test
    public void emptyCardholderNameValidationPutsError(){
        String name = "";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateCardholderName(name, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_NAME);
        String errorValue = errors.get(ERROR_KEY_NAME);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("Cardholder name is required", errorValue);
    }

    @Test
    public void invalidCardholderNameFormatValidationPutsError(){
        String name1 = "1231134";
        String name2 = "ASDfdsdsf";
        String name3 = "asda! asd-a";

        Map<String, String> errors1 = new HashMap<>();
        Map<String, String> errors2 = new HashMap<>();
        Map<String, String> errors3 = new HashMap<>();

        errors1 = validation.validateCardholderName(name1, errors1);
        errors2 = validation.validateCardholderName(name2, errors2);
        errors3 = validation.validateCardholderName(name3, errors3);

        boolean containsKey1 = errors1.containsKey(ERROR_KEY_NAME);
        String errorValue1 = errors1.get(ERROR_KEY_NAME);
        boolean containsKey2 = errors2.containsKey(ERROR_KEY_NAME);
        String errorValue2 = errors2.get(ERROR_KEY_NAME);
        boolean containsKey3 = errors3.containsKey(ERROR_KEY_NAME);
        String errorValue3 = errors3.get(ERROR_KEY_NAME);

        Assertions.assertTrue(containsKey1);
        Assertions.assertTrue(containsKey2);
        Assertions.assertTrue(containsKey3);
        Assertions.assertEquals("Invalid cardholder name format", errorValue1);
        Assertions.assertEquals("Invalid cardholder name format", errorValue2);
        Assertions.assertEquals("Invalid cardholder name format", errorValue3);
    }

    @Test
    public void validCardholderNameValidationTest(){
        String name = "Steve Carrel";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateCurrencyField(name, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_NAME);
        String errorValue = errors.get(ERROR_KEY_NAME);

        Assertions.assertFalse(containsKey);
        Assertions.assertNull(errorValue);
    }

    @Test
    public void emptyCardholderEmailValidationPutsError(){
        String email = "";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateCardholderEmail(email, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_EMAIL);
        String errorValue = errors.get(ERROR_KEY_EMAIL);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("Email is required", errorValue);
    }

    @Test
    public void invalidCardholderEmailFormatValidationPutsError(){
        String email1 = "@qweqweasd";
        String email2 = "asdasd1231";
        String email3 = "qw!!!e@123";

        Map<String, String> errors1 = new HashMap<>();
        Map<String, String> errors2 = new HashMap<>();
        Map<String, String> errors3 = new HashMap<>();

        errors1 = validation.validateCardholderEmail(email1, errors1);
        errors2 = validation.validateCardholderEmail(email2, errors2);
        errors3 = validation.validateCardholderEmail(email3, errors3);

        boolean containsKey1 = errors1.containsKey(ERROR_KEY_EMAIL);
        String errorValue1 = errors1.get(ERROR_KEY_EMAIL);
        boolean containsKey2 = errors2.containsKey(ERROR_KEY_EMAIL);
        String errorValue2 = errors2.get(ERROR_KEY_EMAIL);
        boolean containsKey3 = errors3.containsKey(ERROR_KEY_EMAIL);
        String errorValue3 = errors3.get(ERROR_KEY_EMAIL);

        Assertions.assertTrue(containsKey1);
        Assertions.assertEquals("Invalid cardholder email format", errorValue1);
        Assertions.assertTrue(containsKey2);
        Assertions.assertEquals("Invalid cardholder email format", errorValue2);
        Assertions.assertTrue(containsKey3);
        Assertions.assertEquals("Invalid cardholder email format", errorValue3);
    }


    @Test
    public void validCardholderEmailValidationTest(){
        String email = "email@domain.com";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateCardholderEmail(email, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_EMAIL);
        String errorValue = errors.get(ERROR_KEY_EMAIL);

        Assertions.assertFalse(containsKey);
        Assertions.assertNull(errorValue);
    }

    @Test
    public void emptyCardPanValidationPutsError(){
        String pan = "";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateCardPan(pan, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_PAN);
        String errorValue = errors.get(ERROR_KEY_PAN);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("Pan is required", errorValue);
    }

    @Test
    public void invalidCardPanFormatValidationPutsError(){
        String pan1 = "asdasdlksldmflsdf";
        String pan2 = "1123";

        Map<String, String> errors1 = new HashMap<>();
        Map<String, String> errors2 = new HashMap<>();

        errors1 = validation.validateCardPan(pan1, errors1);
        errors2 = validation.validateCardPan(pan2, errors2);

        boolean containsKey1 = errors1.containsKey(ERROR_KEY_PAN);
        boolean containsKey2 = errors2.containsKey(ERROR_KEY_PAN);
        String errorValue1 = errors1.get(ERROR_KEY_PAN);
        String errorValue2 = errors2.get(ERROR_KEY_PAN);

        Assertions.assertTrue(containsKey1);
        Assertions.assertTrue(containsKey2);
        Assertions.assertEquals("Invalid card pan format", errorValue1);
        Assertions.assertEquals("Invalid card pan format", errorValue2);
    }

    @Test
    public void invalidCardPanLuhnCheckPutsError(){
        String pan = "0000000000000000";

        Map<String, String> errors = new HashMap<>();

        errors = validation.luhnCheck(pan, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_PAN);
        String errorValue = errors.get(ERROR_KEY_PAN);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("Invalid pan number", errorValue);
    }

    @Test
    public void validPanValidationTest(){
        String pan = "5277029120773860";

        Map<String, String> errors = new HashMap<>();
        errors = validation.luhnCheck(pan, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_PAN);
        String errorValue = errors.get(ERROR_KEY_PAN);

        Assertions.assertFalse(containsKey);
        Assertions.assertNull(errorValue);
    }

    @Test
    public void emptyExpiryValidationPutsError(){
        String expiry = "";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateCardExpiryNotEmpty(expiry, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_EXPIRY);
        String errorValue = errors.get(ERROR_KEY_EXPIRY);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("Expiry is required", errorValue);
    }

    @Test
    public void invalidExpiryFormatValidationPutsError(){
        String expiry1 = "qwer";
        String expiry2 = "125";

        Map<String, String> errors1 = new HashMap<>();
        Map<String, String> errors2 = new HashMap<>();

        errors1 = validation.validateCardExpiryFormat(expiry1, errors1);
        errors2 = validation.validateCardExpiryFormat(expiry2, errors2);

        boolean containsKey1 = errors1.containsKey(ERROR_KEY_EXPIRY);
        boolean containsKey2 = errors2.containsKey(ERROR_KEY_EXPIRY);
        String errorValue1 = errors1.get(ERROR_KEY_EXPIRY);
        String errorValue2 = errors2.get(ERROR_KEY_EXPIRY);

        Assertions.assertTrue(containsKey1);
        Assertions.assertTrue(containsKey2);
        Assertions.assertEquals("Invalid card expiry format", errorValue1);
        Assertions.assertEquals("Invalid card expiry format", errorValue2);
    }

    @Test
    public void expiredExpiryValidationPutsError(){
        String expiry = "1221";

        Map<String, String> errors = new HashMap<>();

        errors = validation.checkExpiryOverdue(expiry, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_EXPIRY);
        String errorValue = errors.get(ERROR_KEY_EXPIRY);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("Card is expired", errorValue);
    }

    @Test
    public void validExpiryValidationTest(){
        String expiry = "1225";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateCardExpiry(expiry, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_EXPIRY);
        String errorValue = errors.get(ERROR_KEY_EXPIRY);

        Assertions.assertFalse(containsKey);
        Assertions.assertNull(errorValue);
    }

    @Test
    public void emptyCvvValidationPutsError(){
        String cvv = "";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateCardCvv(cvv, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_CVV);
        String errorValue = errors.get(ERROR_KEY_CVV);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("CVV is required",errorValue);
    }

    @Test
    public void invalidCvvFormatValidationPutsError(){
        String cvv = "2a";

        Map<String, String> errors = new HashMap<>();

        errors = validation.validateCardCvv(cvv, errors);

        boolean containsKey = errors.containsKey(ERROR_KEY_CVV);
        String errorValue = errors.get(ERROR_KEY_CVV);

        Assertions.assertTrue(containsKey);
        Assertions.assertEquals("Invalid card cvv format",errorValue);
    }

}
