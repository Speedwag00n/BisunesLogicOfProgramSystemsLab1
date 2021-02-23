package ilia.nemankov.repository.specification;

import ilia.nemankov.model.Reservation;
import ilia.nemankov.model.Rooms;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class ReservationSpecification implements Specification<Reservation> {

    private final Integer roomsId;
    private final Date arrivalDate;
    private final Date departureDate;

    @Override
    public Predicate toPredicate(Root<Reservation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        Rooms rooms = new Rooms();
        rooms.setId(roomsId);

        predicates.add(criteriaBuilder.equal(root.get("configuration").get("rooms"), rooms));
        predicates.add(
                criteriaBuilder.or(
                        criteriaBuilder.and(criteriaBuilder.lessThan(root.get("arrivalDate"), arrivalDate), criteriaBuilder.lessThan(root.get("departureDate"), arrivalDate)),
                        criteriaBuilder.and(criteriaBuilder.greaterThan(root.get("arrivalDate"), departureDate), criteriaBuilder.greaterThan(root.get("departureDate"), departureDate))
                ).not());

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}
