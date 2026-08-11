package com.miniProject.AeroScale.Repository;

import com.miniProject.AeroScale.Entity.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByEmailIgnoreCase(String email);



    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNo(String phoneNo);
}
