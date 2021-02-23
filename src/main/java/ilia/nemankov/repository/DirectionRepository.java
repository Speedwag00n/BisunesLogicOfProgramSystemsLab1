package ilia.nemankov.repository;

import ilia.nemankov.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectionRepository extends JpaRepository<City, Integer> {

}
