package com.gachokaerick.eshop.orders.repository;

import com.gachokaerick.eshop.orders.domain.Address;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class AddressSpecification {

    public static Specification<Address> getAddressSpecification(
        List<Long> ids,
        String street,
        String city,
        String town,
        String country,
        String zipcode,
        String login,
        String term
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (ids != null) {
                predicates.add(root.get("id").in(ids));
            }
            if (street != null) {
                predicates.add(criteriaBuilder.equal(root.get("street"), street));
            }
            if (city != null) {
                predicates.add(criteriaBuilder.equal(root.get("city"), city));
            }
            if (town != null) {
                predicates.add(criteriaBuilder.equal(root.get("town"), town));
            }
            if (country != null) {
                predicates.add(criteriaBuilder.equal(root.get("country"), country));
            }
            if (zipcode != null) {
                predicates.add(criteriaBuilder.equal(root.get("zipcode"), zipcode));
            }
            if (login != null) {
                predicates.add(criteriaBuilder.equal(root.get("buyer").get("user").get("login"), login));
            }
            if (term != null) {
                String likeTerm = "%" + term + "%";
                predicates.add(
                    criteriaBuilder.or(
                        criteriaBuilder.like(root.get("street"), likeTerm),
                        criteriaBuilder.like(root.get("city"), likeTerm),
                        criteriaBuilder.like(root.get("town"), likeTerm),
                        criteriaBuilder.like(root.get("country"), likeTerm),
                        criteriaBuilder.like(root.get("zipcode"), likeTerm)
                    )
                );
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
