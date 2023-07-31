package org.product.util;

import jakarta.persistence.criteria.Predicate;
import org.product.model.Product;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductSearch {

    public static Specification<Product> search(Map<String,String> fieldNamesWithKeyWords) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            fieldNamesWithKeyWords.entrySet().stream().forEach(entry ->{
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(entry.getKey())), "%" + entry.getValue().toLowerCase() + "%"));
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
