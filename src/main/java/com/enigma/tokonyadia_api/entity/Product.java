package com.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "m_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "price", columnDefinition = "bigint check(price > 0)")
    private Long price;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

}
