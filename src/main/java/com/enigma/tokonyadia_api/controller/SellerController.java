package com.enigma.tokonyadia_api.controller;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.dto.request.SearchSellerRequest;
import com.enigma.tokonyadia_api.dto.request.SellerCreateRequest;
import com.enigma.tokonyadia_api.dto.request.SellerRequest;
import com.enigma.tokonyadia_api.dto.response.SellerResponse;
import com.enigma.tokonyadia_api.service.SellerService;
import com.enigma.tokonyadia_api.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Constant.SELLER_API)
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    @PostMapping
    public ResponseEntity<?> create(SellerCreateRequest request) {
        SellerResponse sellerResponse = sellerService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, Constant.SUCCESS_CREATE_SELLER, sellerResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSellerById(@PathVariable String id) {
        SellerResponse sellerResponse = sellerService.getSellerById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Successfully retrieve seller", sellerResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAllSeller(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ) {
        SearchSellerRequest searchSellerRequest = SearchSellerRequest.builder()
                .page(page)
                .size(size)
                .query(query)
                .sortBy(sortBy)
                .build();
        Page<SellerResponse> sellerResponses = sellerService.getAllSellers(searchSellerRequest);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_GET_ALL_SELLER, sellerResponses);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('SELLER') and @permissionEvaluationServiceImpl.hasAccessToCustomer(#id, authentication.principal.id))")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSeller(@PathVariable String id, @RequestBody SellerRequest request) {
        SellerResponse sellerResponse = sellerService.updateSeller(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_UPDATE_SELLER, sellerResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeller(@PathVariable String id) {
        sellerService.deleteSeller(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, Constant.SUCCESS_DELETE_SELLER, null);
    }
}
