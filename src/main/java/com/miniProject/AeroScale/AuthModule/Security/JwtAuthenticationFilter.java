package com.miniProject.AeroScale.AuthModule.Security;

import com.miniProject.AeroScale.AuthModule.Entity.JwtTokenValidating;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String ATTRIBUTE_NAME_IN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);

        if(token != null && token.length() > 0) {
            JwtTokenValidating jwtTokenValidating = jwtUtils.validateToken(token);

            if(jwtTokenValidating.equals(JwtTokenValidating.EXPIRED)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("""
                        {"error":"TOKEN_EXPIRED","message":"Access token has expired"}
                        """);
                return;
            }

            if(jwtTokenValidating.equals(JwtTokenValidating.VALID)){
                String email = jwtUtils.extractEmail(token);
                UUID id = jwtUtils.extractUserId(token);
                String role = jwtUtils.extractRole(token);


                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        new AuthenticatedObject(id, email), null, authorities);

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);

    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(ATTRIBUTE_NAME_IN_HEADER);

        if(header != null && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public record AuthenticatedObject(UUID id, String email) {}
}
