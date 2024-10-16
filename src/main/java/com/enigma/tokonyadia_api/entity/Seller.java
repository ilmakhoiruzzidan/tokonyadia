package com.enigma.tokonyadia_api.entity;

import com.enigma.tokonyadia_api.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_seller")
public class Seller extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

}
