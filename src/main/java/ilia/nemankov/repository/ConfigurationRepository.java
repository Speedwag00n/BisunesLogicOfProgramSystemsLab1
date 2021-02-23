package ilia.nemankov.repository;

import ilia.nemankov.model.Configuration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

    List<Configuration> findAll(Specification<Configuration> specification);

}
