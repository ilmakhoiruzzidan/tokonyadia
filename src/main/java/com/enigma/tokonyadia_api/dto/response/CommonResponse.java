package com.enigma.tokonyadia_api.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    private Integer status;
    private String message;
    private T data;
    private PagingResponse paging;
}
