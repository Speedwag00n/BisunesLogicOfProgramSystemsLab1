package ilia.nemankov.repository.specification;

import ilia.nemankov.model.FoodType;
import ilia.nemankov.model.Hotel;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@AllArgsConstructor
public class FindHotelByFoods implements Specification<Hotel> {

    private final List<String> foods;

    @Override
    public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate[] predicates = new Predicate[foods.size()];
        for (int i = 0; i < foods.size(); i++) {
            predicates[i] = (criteriaBuilder.equal(root.join("rooms").join("configurations").get("food"), FoodType.valueOf(foods.get(i))));
        }
        return criteriaBuilder.or(predicates);
    }

}
