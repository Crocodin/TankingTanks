package ubb.dbsm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;


public interface TankRepository extends JpaRepository<Tank, Integer> {
    @Query("SELECT t FROM Tank t JOIN t.manufacturer m WHERE LOWER(t.name) LIKE LOWER(:name) AND m = :manufacturer")
    Page<Tank> findAllByNameAndManufacturer(
            @Param("name") String name,
            @Param("manufacturer") Manufacturer manufacturer,
            Pageable pageable
    );

    boolean existsByNameAndManufacturer(String name, Manufacturer manufacturer);
}
