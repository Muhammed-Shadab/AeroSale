package com.miniProject.AeroScale.Repository;

import com.miniProject.AeroScale.Entity.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByEmailIgnoreCase(String email);

    Optional<Users> findByPhoneNo(String phoneNo);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNo(@NotBlank @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$", message = "Phone number must be a valid E.164 number") String phoneNo);
}
