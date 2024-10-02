package com.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "t_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    private LocalDateTime localDateTime;
    @OneToMany
    private Customer customer;



}
