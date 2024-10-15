package com.enigma.tokonyadia_api.specification;

import com.enigma.tokonyadia_api.dto.request.PagingAndSortingRequest;
import com.enigma.tokonyadia_api.entity.Category;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ProductCategorySpecification {
    public static Specification<Category> getSpecification(PagingAndSortingRequest request) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }
}
