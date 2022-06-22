package org.sample.payment.gateway.service;

import org.sample.payment.gateway.model.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class PaymentTransformationImpl implements PaymentTransformation {

    @Override
    public PaymentRecord transformFromPaymentToPaymentRecord(Payment payment) {

        CardholderRecord cardholderRecord = transformToCardholderRecord(payment.getCardholder());
        CardRecord cardRecord = transformCardToCardRecord(payment.getCard());

        PaymentRecord paymentRecord = new PaymentRecord();

        paymentRecord.setInvoice(payment.getInvoice());
        paymentRecord.setAmount(payment.getAmount());
        paymentRecord.setCurrency(payment.getCurrency());
        paymentRecord.setCardholderRecord(cardholderRecord);
        paymentRecord.setCard(cardRecord);

        return paymentRecord;
    }


    private CardRecord transformCardToCardRecord(Card card) {
        CardRecord cardRecord = new CardRecord();
        cardRecord.setPan(encodeValue(card.getPan()));
        cardRecord.setExpiry(encodeValue(card.getExpiry()));
        return cardRecord;
    }

    private CardholderRecord transformToCardholderRecord(Cardholder cardholder) {
        CardholderRecord cardholderRecord = new CardholderRecord();
        cardholderRecord.setName(encodeValue(cardholder.getName()));
        cardholderRecord.setName(cardholder.getName());
        cardholderRecord.setEmail(cardholder.getEmail());
        return cardholderRecord;
    }

    public PaymentResponse transformPaymentRecordToResponse(PaymentRecord  paymentRecord){
        CardholderResponse cardholderResponse = transformCardholderRecordToResponse(paymentRecord.getCardholderRecord());
        CardResponse cardResponse = transfromCardRecordToResponse(paymentRecord.getCard());
        PaymentResponse paymentResponse = new PaymentResponse();

        paymentResponse.setInvoice(paymentRecord.getInvoice());
        paymentResponse.setAmount(paymentRecord.getAmount());
        paymentResponse.setCurrency(paymentRecord.getCurrency());
        paymentResponse.setCardholder(cardholderResponse);
        paymentResponse.setCard(cardResponse);

        return paymentResponse;
    }


    private CardholderResponse transformCardholderRecordToResponse(CardholderRecord cardholderRecord) {
        CardholderResponse cardholderResponse = new CardholderResponse();
        cardholderResponse.setName(maskCardholderName(cardholderRecord.getName()));
        cardholderResponse.setEmail(cardholderRecord.getEmail());
        return cardholderResponse;
    }

    private String maskCardholderName(String name) {
        return name.replaceAll("[A-Za-z]", "*");
    }

    private CardResponse transfromCardRecordToResponse(CardRecord cardRecord) {
        CardResponse cardResponse = new CardResponse();
        String decryptedPan = decodeValue(cardRecord.getPan());
        cardResponse.setPan(maskPan(decryptedPan));
        String decryptedExpiry = decodeValue(cardRecord.getExpiry());
        cardResponse.setExpiry(maskExpiry(decryptedExpiry));

        return cardResponse;
    }

    public PaymentResponse transformPaymentToPaymentResponse(Payment payment){
        CardholderResponse cardholderResponse = transformToCardholderResponse(payment.getCardholder());
        CardResponse cardResponse = transformCardToCardResponse(payment.getCard());
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setInvoice(payment.getInvoice());
        paymentResponse.setAmount(payment.getAmount());
        paymentResponse.setCurrency(payment.getCurrency());
        paymentResponse.setCardholder(cardholderResponse);
        paymentResponse.setCard(cardResponse);

        return paymentResponse;
    }

    public CardholderResponse transformToCardholderResponse(Cardholder cardholder) {
        CardholderResponse cardholderResponse = new CardholderResponse();
        cardholderResponse.setName(maskCardholderName(cardholder.getName()));
        cardholderResponse.setEmail(cardholder.getEmail());
        return cardholderResponse;
    }

    public CardResponse transformCardToCardResponse(Card card){
        CardResponse cardResponse = new CardResponse();
        cardResponse.setPan(maskPan(card.getPan()));
        cardResponse.setExpiry(maskExpiry(card.getExpiry()));
        return cardResponse;
    }

    public String maskExpiry(String numericValue) {
        return numericValue.replaceAll("\\d", "*");
    }

    public String maskPan(String pan) {
        return "************" + pan.substring(12, 16);
    }

    private String encodeValue(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
    private String decodeValue(String value) {
        return new String(Base64.getDecoder().decode(value));
    }
}
