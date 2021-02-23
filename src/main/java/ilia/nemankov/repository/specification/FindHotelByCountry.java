package ilia.nemankov.repository.specification;

import ilia.nemankov.model.Country;
import ilia.nemankov.model.Hotel;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class FindHotelByCountry implements Specification<Hotel> {

    private final Integer countryId;

    @Override
    public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Country country = new Country();
        country.setId(countryId);
        return criteriaBuilder.and(criteriaBuilder.equal(root.get("city").get("country"), country));
    }

}
