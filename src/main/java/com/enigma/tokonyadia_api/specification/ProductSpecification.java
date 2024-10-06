package com.enigma.tokonyadia_api.specification;

import com.enigma.tokonyadia_api.dto.request.SearchProductRequest;
import com.enigma.tokonyadia_api.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> getSpecification(SearchProductRequest request) {
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();

                if (request.getQuery() != null && !StringUtils.hasText(request.getQuery())) {
                    Predicate predicate = criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getQuery() + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + request.getQuery() + "%"));
                    predicateList.add(predicate);
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
            }
        };
    }
}
