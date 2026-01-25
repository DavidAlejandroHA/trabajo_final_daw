package com.canarycode.appointments.repository;

import com.canarycode.appointments.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = "SELECT * FROM `roles` WHERE name = :paramName",
    countQuery = "SELECT count(*) FROM `roles` WHERE name = :paramName",
    nativeQuery = true)
    Role findByName(@Param("paramName") String name);
}