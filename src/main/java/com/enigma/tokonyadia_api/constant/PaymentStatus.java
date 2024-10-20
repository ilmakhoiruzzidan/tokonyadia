package com.enigma.tokonyadia_api.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    SETTLEMENT("settlement"),
    PENDING("pending"),
    DENY("deny"),
    CANCEL("cancel"),
    EXPIRE("expire");

    private final String description;

    public static PaymentStatus findByDesc(String description){
        for (PaymentStatus value : values()) {
            if (value.description.equalsIgnoreCase(description)){
                return value;
            }
        }
        return null;
    }
}


