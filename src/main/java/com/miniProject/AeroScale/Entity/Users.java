package com.miniProject.AeroScale.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.*;

@Data
@Table(name = "users",
        indexes = {
                @Index(name = "idx_email", columnList = "email", unique = true)
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users {

    @Id
    @UuidGenerator
    @Column(unique = true, updatable = false)
    private UUID id;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true, length = 250)
    private String email;


    @NotBlank
    @Column(nullable = false, length = 200)
    @JsonIgnore
    private String password;

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    private String phoneNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean accountNotLocked = true;

    @Builder.Default
    @Column(nullable = false)
    private int failedLoginAttempt = 0;

    @Column(nullable = false)
    private Instant updatedAt;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant AccountLockedUntil;

    private String lastLoginIp;


    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Seller seller;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Buyer buyer;

    @PrePersist
    protected void init() {
        Instant instant = Instant.now();
        this.createdAt = instant;
        this.updatedAt = instant;
    }

    @PreUpdate
    protected void update() {
        this.updatedAt = Instant.now();
    }

    public boolean isAccountCurrentlyLocked() {
        return !this.accountNotLocked &&
                AccountLockedUntil != null &&
                AccountLockedUntil.isBefore(Instant.now());
    }






}
