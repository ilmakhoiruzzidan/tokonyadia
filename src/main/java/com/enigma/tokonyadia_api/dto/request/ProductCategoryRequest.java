package com.enigma.tokonyadia_api.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategoryRequest {
    private String name;
    private String description;
}
