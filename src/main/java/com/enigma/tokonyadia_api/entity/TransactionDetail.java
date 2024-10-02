package com.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_transaction_detail")
public class TransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Product product;

    @Column(name = "price", nullable = false, columnDefinition = "bigint check (price > 0)")
    private Long price;

    @Column(name = "qty", nullable = false, columnDefinition = "int check (qty > 0)")
    private Integer qty;
}
