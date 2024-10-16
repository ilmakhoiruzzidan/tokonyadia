package com.enigma.tokonyadia_api.constant;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_ADMIN("Admin"),
    ROLE_SELLER("Penjual"),
    ROLE_CUSTOMER("Pelanggan");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public static UserRole findByDescription(String name) {
        for (UserRole value : values()) {
            if (value.description.equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }
}
