package com.enigma.tokonyadia_api.dto.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest{
    private String name;
    private String description;
    private Long price;
    private String storeId;
}
