package com.craftelix.weatherviewer.repository;

import com.craftelix.weatherviewer.entity.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

    Optional<Location> findByIdAndUserId(Long id, Long userId);

    List<Location> findByUserId(Long userId);

}
