package com.enigma.tokonyadia_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleProductResponse {
    private String productId;
    private String productName;
    private Integer stock;
    private List<FileResponse> images;
    private Long price;
    private String categoryName;
    private String storeName;
}
