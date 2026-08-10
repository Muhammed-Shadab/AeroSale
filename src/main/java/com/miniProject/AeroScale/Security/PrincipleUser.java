package com.miniProject.AeroScale.Security;

import com.miniProject.AeroScale.Entity.Users;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
public class PrincipleUser implements UserDetails {
    private final UUID id;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final boolean isAccountLocked;
    private final Collection<? extends GrantedAuthority> authorities;

    PrincipleUser(Users users) {
        this.id = users.getId();
        this.email = users.getEmail();
        this.password = users.getPassword();
        this.enabled = users.isEnabled();
        this.isAccountLocked = users.isAccountCurrentlyLocked();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + users.getRole().name()));
    }

    public static PrincipleUser from(Users users) {return new PrincipleUser(users);}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isAccountLocked;
    }


}
