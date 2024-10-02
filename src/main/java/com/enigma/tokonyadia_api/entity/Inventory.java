package com.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "t_inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany
    @JoinColumn
    private List<Product> product;

    @OneToMany
    @JoinColumn
    private List<Store> store;

    @Column(name = "stock", columnDefinition = "(int check (stock > 0))")
    private Integer stock;
}
