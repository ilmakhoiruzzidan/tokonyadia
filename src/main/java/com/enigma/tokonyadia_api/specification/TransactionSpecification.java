package com.enigma.tokonyadia_api.specification;

import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.entity.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class TransactionSpecification {
    public static Specification<Transaction> getSpecification(PagingAndSortingRequest request) {
        return new Specification<Transaction>() {
            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.conjunction();
            }
        };
    }
}

