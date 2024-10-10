package com.enigma.tokonyadia_api.entity;

import com.enigma.tokonyadia_api.constant.UserRole;
import jakarta.persistence.*;

public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username", unique = true, length = 30)
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;
}
