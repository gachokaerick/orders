package com.gachokaerick.eshop.orders.domain.exception;

public final class APIException extends RuntimeException {

    APIException(String errorMessage) {
        super(errorMessage);
    }

    static String constructMessage(String errorMessage) {
        return "API Exception: " + errorMessage;
    }

    public static APIException throwAPIException(String errorMessage) {
        return new APIException(constructMessage(errorMessage));
    }
}
