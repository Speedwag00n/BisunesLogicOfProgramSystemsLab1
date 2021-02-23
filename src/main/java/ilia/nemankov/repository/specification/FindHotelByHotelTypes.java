package ilia.nemankov.repository.specification;

import ilia.nemankov.model.Hotel;
import ilia.nemankov.model.HotelType;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@AllArgsConstructor
public class FindHotelByHotelTypes implements Specification<Hotel> {

    private final List<String> hotelTypes;

    @Override
    public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate[] predicates = new Predicate[hotelTypes.size()];
        for (int i = 0; i < hotelTypes.size(); i++) {
            predicates[i] = (criteriaBuilder.equal(root.get("hotelType"), HotelType.valueOf(hotelTypes.get(i))));
        }
        return criteriaBuilder.or(predicates);
    }

}
