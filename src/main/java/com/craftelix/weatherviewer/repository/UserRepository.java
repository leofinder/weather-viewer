package com.craftelix.weatherviewer.repository;

import com.craftelix.weatherviewer.entity.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("""
            SELECT users.*
            FROM users
            JOIN sessions ON users.id = sessions.user_id
            WHERE sessions.id = :sessionId
            """)
    Optional<User> findBySessionId(@Param("sessionId") UUID sessionId);
}
