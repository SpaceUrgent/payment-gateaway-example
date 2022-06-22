package org.sample.payment.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.sample.payment.gateway.constants.Constant;
import org.sample.payment.gateway.controller.PaymentController;
import org.sample.payment.gateway.model.*;
import org.sample.payment.gateway.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sample.payment.gateway.constants.Constant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class PaymentIntTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentController controller;

    @Test
    public void test() throws Exception{
        assertThat(controller).isNotNull();
    }

    @Test
    public void testValidPaymentPost() throws Exception{

        Cardholder cardholder = Cardholder.builder()
                .name("First Last")
                .email("email@domain.com")
                .build();
        Card card = Card.builder()
                .pan("5277029120773860")
                .expiry("1224")
                .cvv("123")
                .build();
        Payment payment = Payment.builder()
                .invoice("1234567")
                .amount(1200)
                .currency("USD")
                .cardholder(cardholder)
                .card(card)
                .build();

        CardholderResponse cardholderResponse = CardholderResponse.builder()
                .name("***** ****")
                .email("email@domain.com")
                .build();

        CardResponse cardResponse = CardResponse.builder()
                .pan("************3860")
                .expiry("****")
                .build();

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .invoice("1234567")
                .amount(1200)
                .currency("USD")
                .cardholder(cardholderResponse)
                .card(cardResponse)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(payment);

         this.mvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(new PaymentConfirmation())));

         this.mvc.perform(get("/payment/get")
                        .content(payment.getInvoice())
                        )
                 .andDo(print())
                 .andExpect(status().is2xxSuccessful())
                 .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                 .andExpect(content().json(objectMapper.writeValueAsString(paymentResponse)));

         Map<String, String> errors = new HashMap<>();
         errors.put(ERROR_KEY_INVOICE, "Payment with same invoice exists. Multiple payments with same invoice can not be registered");
         PaymentDecline decline = new PaymentDecline(errors);

         this.mvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(decline)));
    }

    @Test
    public void testInvalidPaymentPost() throws Exception{
        Cardholder cardholder = Cardholder.builder()
                .name("123 Last")
                .email("email@domain.com")
                .build();
        Card card = Card.builder()
                .pan("527702912073860")
                .expiry("1221")
                .cvv("")
                .build();
        Payment payment = Payment.builder()
                .invoice("1234567")
                .amount(-1200)
                .currency("USD")
                .cardholder(cardholder)
                .card(card)
                .build();

        Map<String, String> errors = new HashMap<>();
        errors.put(ERROR_KEY_AMOUNT, "Amount is negative");
        errors.put(ERROR_KEY_NAME, "Invalid cardholder name format");
        errors.put(ERROR_KEY_PAN, "Invalid card pan format");
        errors.put(ERROR_KEY_EXPIRY, "Card is expired");
        errors.put(ERROR_KEY_CVV, "CVV is required");

        PaymentDecline paymentDecline = new PaymentDecline(errors);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(payment);

        this.mvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(paymentDecline)));
    }
}