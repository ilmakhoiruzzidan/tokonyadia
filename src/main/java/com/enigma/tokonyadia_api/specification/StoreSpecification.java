package com.enigma.tokonyadia_api.specification;

import com.enigma.tokonyadia_api.dto.request.SearchStoreRequest;
import com.enigma.tokonyadia_api.entity.Store;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StoreSpecification {
    public static Specification<Store> getSpecification(SearchStoreRequest request) {
        return new Specification<Store>() {
            @Override
            public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (request.getQuery() == null) return criteriaBuilder.conjunction();

                List<Predicate> predicateList = new ArrayList<>();
                if (!StringUtils.hasText(request.getQuery()) || request.getQuery() != null) {
                    Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getQuery() + "%");
                    predicateList.add(predicate);
                }

                return query.where(predicateList.toArray(new Predicate[]{})).getRestriction();
            }
        };
    }
}
