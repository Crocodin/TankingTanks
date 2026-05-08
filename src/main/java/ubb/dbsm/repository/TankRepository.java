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

    @Query(value = "SELECT t.* FROM tank t JOIN manufacturer m ON t.manufacturer_id = m.manufacturer_id WHERE LOWER(t.tank_name) LIKE LOWER(:name) AND t.manufacturer_id = :manufacturerId",
            countQuery = "SELECT COUNT(*) FROM tank t WHERE LOWER(t.tank_name) LIKE LOWER(:name) AND t.manufacturer_id = :manufacturerId",
            nativeQuery = true)
    Page<Tank> findAllByNameAndManufacturerIncludingDeleted(
            @Param("name") String name,
            @Param("manufacturerId") Integer manufacturerId,
            Pageable pageable
    );
}
