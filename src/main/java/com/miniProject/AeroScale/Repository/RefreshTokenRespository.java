package com.miniProject.AeroScale.Repository;

import com.miniProject.AeroScale.Entity.RefreshToken;
import com.miniProject.AeroScale.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RefreshTokenRespository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String hash);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.id = :id")
    void revokeById(@Param("id") UUID id);


    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.token = :hash")
    void revokeIfExistByToken(@Param("hash") String hash);
}
