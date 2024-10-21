package com.enigma.tokonyadia_api.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private List<FileResponse> images;
    private Long price;
    private Integer stock;
    private StoreResponse store;
    private ProductCategoryResponse category;
}
