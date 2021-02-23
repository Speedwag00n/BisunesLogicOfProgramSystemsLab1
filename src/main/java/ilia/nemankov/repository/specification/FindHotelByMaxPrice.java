package ilia.nemankov.repository.specification;

import ilia.nemankov.model.Hotel;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class FindHotelByMaxPrice implements Specification<Hotel> {

    private final Integer maxPrice;

    @Override
    public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.join("rooms").join("configurations").get("price"), maxPrice));
    }

}
