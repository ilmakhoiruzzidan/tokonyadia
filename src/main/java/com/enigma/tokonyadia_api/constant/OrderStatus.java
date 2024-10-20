package com.enigma.tokonyadia_api.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    DRAFT("Draft"),
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    FAILED("Failed"),
    EXPIRE("Expire");

    private final String description;

    public static OrderStatus findByDesc(String desc) {
        for (OrderStatus value : values()) {
            if (value.getDescription().equalsIgnoreCase(desc)) {
                return value;
            }
        }
        return null;
    }
}
