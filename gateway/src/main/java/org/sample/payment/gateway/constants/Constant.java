package org.sample.payment.gateway.constants;

public class Constant {
    public static final String CURRENCY_REGEX ="[A-Z]{3}";
    public static final String CARDHOLDER_NAME_REGEX ="[a-zA-Z]*[\\s]{1}[a-zA-Z].*";
    public static final String EMAIL_REGEX ="^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
    public static final String PAN_REGEX = "[0-9]{16}";
    public static final String EXPIRY_REGEX = "[0-9]{4}";
    public static final String CVV_REGEX = "[0-9]{3}";

    public static final String ERROR_KEY_INVOICE = "invoice:";
    public static final String ERROR_KEY_AMOUNT = "amount:";
    public static final String ERROR_KEY_CURRENCY = "currency:";
    public static final String ERROR_KEY_CARDHOLDER = "cardholder:";
    public static final String ERROR_KEY_NAME = "name:";
    public static final String ERROR_KEY_EMAIL = "email:";
    public static final String ERROR_KEY_CARD = "card:";
    public static final String ERROR_KEY_PAN = "pan:";
    public static final String ERROR_KEY_EXPIRY = "expiry:";
    public static final String ERROR_KEY_CVV = "cvv:";

}
