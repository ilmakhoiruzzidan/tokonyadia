package com.enigma.tokonyadia_api.dto.response;

import com.enigma.tokonyadia_api.entity.Store;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private Long price;
    private Integer stock;
    private StoreResponse store;
    private String categoryId;
    private String categoryName;
}
