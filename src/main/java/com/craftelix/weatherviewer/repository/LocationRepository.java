package com.craftelix.weatherviewer.repository;

import com.craftelix.weatherviewer.entity.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
}
