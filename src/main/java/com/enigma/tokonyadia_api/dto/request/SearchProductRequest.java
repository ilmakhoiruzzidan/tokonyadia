package com.enigma.tokonyadia_api.dto.request;

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
}
