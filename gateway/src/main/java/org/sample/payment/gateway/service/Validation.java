package org.sample.payment.gateway.service;

import org.sample.payment.gateway.model.Payment;

import javax.xml.bind.ValidationException;
import java.util.Map;

public interface Validation {

   public Map<String, String> validatePayment(Payment payment) throws ValidationException;
}
