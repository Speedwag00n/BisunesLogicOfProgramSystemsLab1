package ilia.nemankov.repository.specification;

import ilia.nemankov.model.City;
import ilia.nemankov.model.Hotel;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@AllArgsConstructor
public class FindHotelByCity implements Specification<Hotel> {

    private final Integer cityId;

    @Override
    public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        City city = new City();
        city.setId(cityId);
        return criteriaBuilder.and(criteriaBuilder.equal(root.get("city"), city));
    }

}
