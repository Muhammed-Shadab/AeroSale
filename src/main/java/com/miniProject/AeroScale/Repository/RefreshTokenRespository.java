package com.miniProject.AeroScale.Repository;

import com.miniProject.AeroScale.Entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface RefreshTokenRespository extends JpaRepository<RefreshToken, UUID> {

}
