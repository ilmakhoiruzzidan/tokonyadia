package com.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private Product product;

    @Column
    private Store store;

    @Column(name = "stock", columnDefinition = "(int check (stock > 0))")
    private Integer stock;
}
