package ubb.dbsm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubb.dbsm.domain.Manufacturer;
import ubb.dbsm.repository.ManufacturerRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;

    @Cacheable("manufacturers")
    public Page<Manufacturer> getPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return manufacturerRepository.findAll(pageable);
    }

    @Cacheable(value = "manufacturer", key = "#name")
    public Optional<Manufacturer> findByName(String name) {
        return manufacturerRepository.findByName(name);
    }

    @Transactional
    @CacheEvict(value = {"manufacturers", "manufacturer"}, allEntries = true)
    public void delete(int id) {
        manufacturerRepository.deleteById(id);
    }
}
