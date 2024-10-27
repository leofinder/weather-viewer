package com.craftelix.weatherviewer.repository;

import com.craftelix.weatherviewer.entity.Location;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

    @Query(""" 
            SELECT *
            FROM locations
            WHERE name = :#{#location.name}
                AND latitude = :#{#location.latitude}
                AND longitude = :#{#location.longitude}
                AND user_id = :#{#location.userId}
            LIMIT 1
            """)
    Optional<Location> findByLocationInfo(@Param("location") Location location);

    List<Location> findByUserId(Long userId);

}
