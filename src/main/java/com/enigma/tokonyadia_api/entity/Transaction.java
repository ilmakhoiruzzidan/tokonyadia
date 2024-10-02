package com.enigma.tokonyadia_api.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "t_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "trans_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime transDate;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn
    private List<Customer> customer;

    @PrePersist
    protected void PrePersist() {
        this.transDate = LocalDateTime.now();
    }

}
