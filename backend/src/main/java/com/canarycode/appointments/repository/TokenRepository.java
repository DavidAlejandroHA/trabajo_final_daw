package com.canarycode.appointments.repository;

import com.canarycode.appointments.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    //findAllValidIsFalseOrRevokedIsFalseByUserId
}
