package com.enigma.tokonyadia_api.specification;

import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.entity.Order;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
    public static Specification<Order> getSpecification(PagingAndSortingRequest request) {
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.conjunction();
            }
        };
    }
}

