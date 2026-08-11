package com.miniProject.AeroScale.Repository;

import com.miniProject.AeroScale.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RefreshTokenRespository extends JpaRepository<RefreshToken, UUID> {

    boolean existsByToken(String dkbdbvibvue);
}
