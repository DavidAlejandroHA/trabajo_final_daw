package com.canarycode.appointments.repository;

import com.canarycode.appointments.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = "SELECT * FROM `tokens` WHERE user_id = :paramName AND (expired = 0 OR revoked = 0)",
            countQuery = "SELECT count(*) FROM `tokens` WHERE user_id = :paramName AND (expired = 0 OR revoked = 0)",
            nativeQuery = true)
    //List<Token> findAllValidIsFalseOrRevokedIsFalseByUserId
    List<Token> findAllValidTokensByUserId(@Param("paramName") long userId);

    @Query(value = "SELECT * FROM `tokens` WHERE token = :paramName",
            countQuery = "SELECT count(*) FROM `tokens` WHERE token = :paramName)",
            nativeQuery = true)
        //List<Token> findAllValidIsFalseOrRevokedIsFalseByUserId
    Optional<Token> findByToken(@Param("paramName") String token);
}
