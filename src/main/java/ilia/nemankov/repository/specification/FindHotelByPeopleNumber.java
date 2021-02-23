package ilia.nemankov.repository.specification;

import ilia.nemankov.model.Configuration;
import ilia.nemankov.model.Hotel;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class FindHotelByPeopleNumber implements Specification<Hotel> {

    private final Integer peopleNumber;

    @Override
    public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Configuration configuration = new Configuration();
        configuration.setCapacity(peopleNumber);
        return criteriaBuilder.and(criteriaBuilder.equal(root.join("rooms").join("configurations").get("capacity"), peopleNumber));
    }

}
