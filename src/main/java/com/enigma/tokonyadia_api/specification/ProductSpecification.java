package com.enigma.tokonyadia_api.specification;

import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.entity.Category;
import com.enigma.tokonyadia_api.entity.Product;
import com.enigma.tokonyadia_api.entity.Store;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductSpecification {
    public static Specification<Product> getSpecification(SearchProductRequest request) {
        return (root, query, criteriaBuilder) -> {
            if (request.getQuery() == null) {
                return criteriaBuilder.conjunction();
            }
            List<Predicate> predicateList = new ArrayList<>();
            Join<Product, Store> storeJoin = root.join("store");
            Join<Product, Category> categoryJoin = root.join("category");

            if (request.getQuery() != null && StringUtils.hasText(request.getQuery())) {
                String lowerQuery = request.getQuery().toLowerCase();
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getQuery().toLowerCase() + "%");
                Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + request.getQuery().toLowerCase() + "%");
                Predicate storePredicate = criteriaBuilder.like(criteriaBuilder.lower(storeJoin.get("name")), "%" + lowerQuery + "%");
                Predicate categoryPredicate = criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("name")), "%" + lowerQuery + "%");
                predicateList.add(criteriaBuilder.or(namePredicate, descriptionPredicate, storePredicate, categoryPredicate));
            }

            if (request.getMinPrice() != null && request.getMaxPrice() != null) {
                Predicate predicate = criteriaBuilder.between(root.get("price"), request.getMinPrice(), request.getMaxPrice());
                predicateList.add(predicate);
            } else if (request.getMinPrice() != null) {
                Predicate predicate = criteriaBuilder.between(root.get("price"), request.getMinPrice(), Long.MAX_VALUE);
                predicateList.add(predicate);
            } else if (request.getMaxPrice() != null) {
                Predicate predicate = criteriaBuilder.between(root.get("price"), 0L, request.getMaxPrice());
                predicateList.add(predicate);
            }

            if (predicateList.isEmpty()) return criteriaBuilder.conjunction();

            return criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
        };
    }
}
