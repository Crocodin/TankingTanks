package ubb.dbsm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.domain.Tank;
import ubb.dbsm.repository.ManufacturerRepository;
import ubb.dbsm.repository.TankRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TankService {
    private final ManufacturerRepository manufacturerRepository;
    private final TankRepository tankRepository;

    @Cacheable(value = "tanks")
    public Page<Tank> findByNameAndManufacturer(String name, Manufacturer manufacturer, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return tankRepository.findAllByNameAndManufacturer("%" + name + "%", manufacturer, pageable);
    }

    @Transactional(readOnly = false)
    @CacheEvict(value = "tanks", allEntries = true)
    public void save(String name, String Year, Manufacturer manufacturer) throws DuplicateKeyException, NumberFormatException {
        Tank tank = Tank.builder()
            .name(name)
            .yearOfProduction(Integer.parseInt(Year))
            .manufacturer(manufacturer)
            .build();

        log.debug("Saving Tank with name {}", tank.getName());
        tankRepository.save(tank);
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
}
