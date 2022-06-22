package org.sample.payment.gateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sample.payment.gateway.service.PaymentTransformationImpl;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaymentTransformationTest {

    PaymentTransformationImpl paymentDTO = new PaymentTransformationImpl();

    @Test
    public void maskCardExpiryTest(){

        String cardExpiry = "0224";
        String maskedExpiry = paymentDTO.maskExpiry(cardExpiry);

        Assertions.assertEquals("****", maskedExpiry);
    }

    @Test
    public void maskCardPanTest(){

        String cardPan = "1212000000001234";
        String maskedPan = paymentDTO.maskPan(cardPan);

        Assertions.assertEquals("************1234", maskedPan);
    }
}
