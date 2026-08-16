package com.miniProject.AeroScale.AuthModule.Controller.Security;

import com.miniProject.AeroScale.AuthModule.Controller.Entity.Users;
import com.miniProject.AeroScale.AuthModule.Controller.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("user doesn't exist with this email"));

        return PrincipleUser.from(user);
    }
}
