package org.sample.payment.gateway.constants;

public class Constant {
    public static final String CURRENCY_REGEX ="[A-Z]{3}";
    public static final String CARDHOLDER_NAME_REGEX ="[a-zA-Z]*[\\s]{1}[a-zA-Z].*";
    public static final String EMAIL_REGEX ="^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
    public static final String PAN_REGEX = "[0-9]{16}";
    public static final String EXPIRY_REGEX = "[0-9]{4}";
    public static final String CVV_REGEX = "[0-9]{3}";

}
