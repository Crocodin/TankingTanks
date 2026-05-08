package ubb.dbsm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ubb.dbsm.domain.Country;

public interface CountryRepository extends JpaRepository<Country, Integer> {
}
