package ilia.nemankov.repository.specification;

import ilia.nemankov.model.Hotel;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@AllArgsConstructor
public class FindHotelByStars implements Specification<Hotel> {

    private List<Integer> stars;

    @Override
    public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate[] predicates = new Predicate[stars.size()];
        for (int i = 0; i < stars.size(); i++) {
            predicates[i] = (criteriaBuilder.equal(root.get("stars"), stars.get(i)));
        }
        return criteriaBuilder.or(predicates);
    }

}
