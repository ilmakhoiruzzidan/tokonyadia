package com.enigma.tokonyadia_api.entity;

import com.enigma.tokonyadia_api.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "m_category")
public class Category extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 100)
    private String description;
}
