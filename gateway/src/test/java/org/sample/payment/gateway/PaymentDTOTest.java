package org.sample.payment.gateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sample.payment.gateway.service.PaymentDTOImpl;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaymentDTOTest {

    PaymentDTOImpl paymentDTO = new PaymentDTOImpl();

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
