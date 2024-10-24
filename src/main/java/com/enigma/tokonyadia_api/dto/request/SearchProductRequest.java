package com.enigma.tokonyadia_api.dto.request;

import com.enigma.tokonyadia_api.dto.response.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SearchProductRequest extends PagingAndSortingRequest {
    private String query;
    private Long minPrice;
    private Long maxPrice;
    private String store;
    private String categoryId;
    private String categoryName;
}
