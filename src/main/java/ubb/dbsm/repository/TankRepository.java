package ubb.dbsm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;

import java.util.Optional;

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

    @Modifying
    @Query(value = "DELETE FROM tank WHERE tank_id = :id", nativeQuery = true)
    void hardDeleteById(@Param("id") Integer id);

    @Query(value = "SELECT * FROM tank WHERE LOWER(tank_name) = LOWER(:name) AND manufacturer_id = :manufacturerId AND is_deleted = 1", nativeQuery = true)
    Optional<Tank> findDeletedByNameAndManufacturer(
            @Param("name") String name,
            @Param("manufacturerId") Integer manufacturerId
    );

    @Modifying
    @Query(value = "UPDATE tank SET is_deleted = 0, deleted_at = NULL, deleted_by = NULL WHERE tank_id = :id", nativeQuery = true)
    void restoreById(@Param("id") Integer id);
}
