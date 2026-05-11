package ubb.dbsm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.domain.User;
import ubb.dbsm.repository.ManufacturerRepository;
import ubb.dbsm.repository.TankRepository;
import ubb.dbsm.utils.AlertUtil;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TankService {
    private final ManufacturerRepository manufacturerRepository;
    private final TankRepository tankRepository;

    @Cacheable(value = "tanks", key = "#name + #manufacturer.id + #pageNumber + #isAdmin")
    public Page<Tank> findByNameAndManufacturer(String name, Manufacturer manufacturer, int pageNumber, int pageSize, boolean isAdmin) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        System.out.println("++++++++++++++ " + isAdmin);
        if (isAdmin) {
            log.debug("Searching for manufacturer with name {} as admin", name);
            return tankRepository.findAllByNameAndManufacturerIncludingDeleted( '%' + name + '%', manufacturer.getId(), pageable);
        }
        log.debug("Searching for manufacturer with name {} as user", name);
        return tankRepository.findAllByNameAndManufacturer("%" + name + "%", manufacturer, pageable);
    }

    @Transactional(readOnly = false)
    @CacheEvict(value = "tanks", allEntries = true)
    public void save(String name, String year, Manufacturer manufacturer) throws DuplicateKeyException, NumberFormatException {
        try {
            Optional<Tank> deletedTank = tankRepository.findDeletedByNameAndManufacturer(name, manufacturer.getId());

            if (deletedTank.isPresent()) {
                log.debug("Tank with name {} already exists but is deleted, restoring it", name);
                Tank tank = deletedTank.get();
                tankRepository.restoreById(tank.getId());
                tank.setDeletedAt(null);
                tank.setDeletedBy(null);
                tank.setYearOfProduction(Integer.parseInt(year));
                tankRepository.save(tank);
                log.debug("Restored Tank with name {}", name);
            } else {
                Tank tank = Tank.builder()
                        .name(name)
                        .yearOfProduction(Integer.parseInt(year))
                        .manufacturer(manufacturer)
                        .build();

                log.debug("Saving Tank with name {}", tank.getName());
                tankRepository.saveAndFlush(tank);
                log.debug("Saved Tank with name {}", tank.getName());
            }
        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("Saving Tank with name {} failed", name, e);
            AlertUtil.showConflictError();
        }
    }

    @Transactional(readOnly = false)
    @CacheEvict(value = "tanks", allEntries = true)
    public void update(Tank tank) {
        log.debug("Updating Tank with name {}", tank.getName());
        tankRepository.save(tank);
    }

    @Transactional(readOnly = false)
    @CacheEvict(value = "tanks", allEntries = true)
    public void delete(int id) {
        log.debug("Deleting Tank with id {}", id);
        tankRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    @CacheEvict(value = "tanks", allEntries = true)
    public void delete(int id, User loggedUser) {
        if  (loggedUser.isAdmin()) {
            log.warn("Admin hard deleting tank with id: {}", id);
            tankRepository.hardDeleteById(id);
        } else {
            log.debug("Soft deleting Tank with id {}, by {}", id, loggedUser.getUsername());
            Tank tank = tankRepository.findById(id).orElseThrow();
            tank.setDeletedAt(LocalDateTime.now());
            tank.setDeletedBy(loggedUser.getUsername());
            tankRepository.save(tank);
            tankRepository.delete(tank); // @SoftDelete handles the actual flag
        }
    }
}
