package com.canarycode.appointments.repository;

import com.canarycode.appointments.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM `users` WHERE nickName = :paramName",
            countQuery = "SELECT count(*) FROM `users` WHERE nickName = :paramName",
            nativeQuery = true)
    Optional<User> findByUsername(@Param("paramName") String name);

    @Query(value = "SELECT * FROM `users` WHERE email = :paramName",
            countQuery = "SELECT count(*) FROM `users` WHERE email = :paramName",
            nativeQuery = true)
    Optional<User> findByEmail(@Param("paramName") String email);
}
