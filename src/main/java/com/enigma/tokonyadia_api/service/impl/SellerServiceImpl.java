package com.enigma.tokonyadia_api.service.impl;

import com.enigma.tokonyadia_api.constant.Constant;
import com.enigma.tokonyadia_api.constant.UserRole;
import com.enigma.tokonyadia_api.dto.mapper.Mapper;
import com.enigma.tokonyadia_api.dto.request.SearchSellerRequest;
import com.enigma.tokonyadia_api.dto.request.SellerCreateRequest;
import com.enigma.tokonyadia_api.dto.request.SellerRequest;
import com.enigma.tokonyadia_api.dto.response.SellerResponse;
import com.enigma.tokonyadia_api.entity.Seller;
import com.enigma.tokonyadia_api.entity.UserAccount;
import com.enigma.tokonyadia_api.repository.SellerRepository;
import com.enigma.tokonyadia_api.service.SellerService;
import com.enigma.tokonyadia_api.service.UserService;
import com.enigma.tokonyadia_api.specification.SellerSpecification;
import com.enigma.tokonyadia_api.util.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final UserService userService;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SellerResponse create(SellerCreateRequest request) {
        UserAccount userAccount = UserAccount.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_SELLER)
                .build();

        userService.create(userAccount);

        Seller seller = Seller.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .userAccount(userAccount)
                .build();
        sellerRepository.saveAndFlush(seller);
        return Mapper.toSellerResponse(seller);
    }

    @Override
    public Seller create(Seller seller) {
        return sellerRepository.saveAndFlush(seller);
    }

    @Override
    public SellerResponse getSellerById(String id) {
        Seller seller = getOne(id);
        return Mapper.toSellerResponse(seller);
    }

    @Override
    public Page<SellerResponse> getAllSellers(SearchSellerRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Seller> customerSpecification = SellerSpecification.getSpecification(request);
        Page<Seller> customerPage = sellerRepository.findAll(customerSpecification, pageable);
        return customerPage.map(Mapper::toSellerResponse);

    }

    @Override
    public SellerResponse updateSeller(String id, SellerRequest request) {
        Seller newSeller = getOne(id);
        newSeller.setName(request.getName());
        newSeller.setEmail(request.getEmail());
        newSeller.setPhoneNumber(request.getPhoneNumber());
        sellerRepository.save(newSeller);
        return Mapper.toSellerResponse(newSeller);
    }

    @Override
    public void deleteSeller(String id) {
        Seller seller = getOne(id);
        if (seller == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_SELLER_NOT_FOUND);
        sellerRepository.deleteById(id);
    }

    @Override
    public Seller getOne(String id) {
        Optional<Seller> seller = sellerRepository.findById(id);
        return seller.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Constant.ERROR_SELLER_NOT_FOUND));
    }
}
