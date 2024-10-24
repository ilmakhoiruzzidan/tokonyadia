package com.enigma.tokonyadia_api.entity;

import com.enigma.tokonyadia_api.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_store")
public class Store extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "no_siup", nullable = false, unique = true, length = 20)
    private String noSiup;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @OneToMany(mappedBy = "store")
    private List<Product> product;

    @OneToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

}