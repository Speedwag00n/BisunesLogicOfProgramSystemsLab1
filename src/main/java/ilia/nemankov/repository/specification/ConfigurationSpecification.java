package ilia.nemankov.repository.specification;

import ilia.nemankov.model.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ConfigurationSpecification implements Specification<Configuration> {

    private final Integer cityId;
    private final Integer countryId;
    private final List<String> hotelTypes;
    private final List<Integer> stars;
    private final List<String> foods;
    private final Integer peopleNumber;
    private final Integer minPrice;
    private final Integer maxPrice;

    @Override
    public Predicate toPredicate(Root<Configuration> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (cityId != null) {
            City city = new City();
            city.setId(cityId);
            predicates.add(criteriaBuilder.equal(root.get("rooms").get("hotel").get("city"), city));
        } else {
            Country country = new Country();
            country.setId(countryId);
            predicates.add(criteriaBuilder.equal(root.get("rooms").get("hotel").get("city").get("country"), country));
        }

        if (hotelTypes != null && hotelTypes.size() != 0) {
            Predicate[] hotelTypePredicates = new Predicate[hotelTypes.size()];
            for (int i = 0; i < hotelTypes.size(); i++) {
                hotelTypePredicates[i] = criteriaBuilder.equal(root.get("rooms").get("hotel").get("hotelType"), HotelType.valueOf(hotelTypes.get(i)));
            }
            predicates.add(criteriaBuilder.or(hotelTypePredicates));
        }

        if (stars != null && stars.size() != 0) {
            Predicate[] starsPredicates = new Predicate[stars.size()];
            for (int i = 0; i < stars.size(); i++) {
                starsPredicates[i] = criteriaBuilder.equal(root.get("rooms").get("hotel").get("stars"), stars.get(i));
            }
            predicates.add(criteriaBuilder.or(starsPredicates));
        }

        if (foods != null && foods.size() != 0) {
            Predicate[] foodPredicates = new Predicate[foods.size()];
            for (int i = 0; i < foods.size(); i++) {
                foodPredicates[i] = criteriaBuilder.equal(root.get("food"), FoodType.valueOf(foods.get(i)));
            }
            predicates.add(criteriaBuilder.or(foodPredicates));
        }

        if (peopleNumber != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("capacity"), peopleNumber));
        }

        if (minPrice != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}
