package com.enigma.tokonyadia_api.specification;

import com.enigma.tokonyadia_api.dto.request.SearchSellerRequest;
import com.enigma.tokonyadia_api.entity.Seller;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SellerSpecification {
    public static Specification<Seller> getSpecification(SearchSellerRequest request){
        return (root, query, criteriaBuilder) -> {
            if (request.getQuery() == null) return criteriaBuilder.conjunction();

            List<Predicate> predicateList = new ArrayList<>();
            if (!StringUtils.hasText(request.getQuery())) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getQuery() + "%");
                predicateList.add(predicate);
            }

            if (request.getQuery() != null) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getQuery() + "%");
                predicateList.add(predicate);
            }

            if (predicateList.isEmpty()) return criteriaBuilder.conjunction();
            return query.where(predicateList.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
