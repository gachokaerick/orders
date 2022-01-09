package com.gachokaerick.eshop.orders.domain.exception;

public final class DomainException extends RuntimeException {

    DomainException(String errorMessage) {
        super(errorMessage);
    }

    static String constructMessage(String domain, String errorMessage) {
        return "Domain Exception: " + domain + " - " + errorMessage;
    }

    public static DomainException throwDomainException(String domain, String errorMessage) {
        return new DomainException(constructMessage(domain, errorMessage));
    }
}
