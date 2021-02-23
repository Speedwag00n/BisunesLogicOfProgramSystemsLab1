package ilia.nemankov.repository.specification;

import ilia.nemankov.model.Hotel;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class FindHotelByMinPrice implements Specification<Hotel> {

    private final Integer minPrice;

    @Override
    public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.join("rooms").join("configurations").get("price"), minPrice));
    }

}
