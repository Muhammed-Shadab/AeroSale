package com.miniProject.AeroScale.AuthModule.Repository;

import com.miniProject.AeroScale.AuthModule.Entity.RefreshToken;
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
