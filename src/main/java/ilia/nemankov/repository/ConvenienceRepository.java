package ilia.nemankov.repository;

import ilia.nemankov.model.Convenience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvenienceRepository extends JpaRepository<Convenience, Integer> {

}
