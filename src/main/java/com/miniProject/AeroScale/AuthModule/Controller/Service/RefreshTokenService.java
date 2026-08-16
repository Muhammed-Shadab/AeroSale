package com.miniProject.AeroScale.AuthModule.Controller.Service;

import com.miniProject.AeroScale.AuthModule.Controller.Entity.RefreshToken;
import com.miniProject.AeroScale.AuthModule.Controller.Entity.Users;
import com.miniProject.AeroScale.AuthModule.Controller.Exception.RefreshTokenException;
import com.miniProject.AeroScale.AuthModule.Controller.Repository.RefreshTokenRespository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh-token-byteLength}")
    public int TOKEN_BYTE_LENGTH;

    @Value("${jwt.refresh-token-expiration-ms}")
    private Long expiresDurationInMilli;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final RefreshTokenRespository refreshTokenRespository;


    @Transactional
    public String generateRefreshToken(Users user, String deviceInfo, String userIp) {
        String rawToken = generateRawToken();
        String hash = sha256Hex(rawToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .users(user)
                .token(hash)
                .expiresAt(Instant.now().plus(Duration.ofMillis(expiresDurationInMilli)))
                .deviceInfo(deviceInfo)
                .issuedIp(userIp)
                .build();


        refreshTokenRespository.save(refreshToken);
        return rawToken;
    }

    private String generateRawToken() {
        byte[] bytes = new byte[TOKEN_BYTE_LENGTH];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String sha256Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }


    @Transactional
    public Users validateRefreshToken(String token) {
        String hash = sha256Hex(token);

        RefreshToken refreshToken = refreshTokenRespository.findByToken(hash)
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found!"));


        if(refreshToken.isExpired()) {
            throw new RefreshTokenException("Unable to create the Access token because Refesh token is Expired");
        }

        if(!refreshToken.isUsable()) {
            throw new RefreshTokenException("Refresh Token is Already Revoked!!");
        }
        refreshTokenRespository.revokeById(refreshToken.getId());
        return refreshToken.getUsers();
    }

    public void revoke(String token) {
        String hash = sha256Hex(token);
        refreshTokenRespository.revokeIfExistByToken(hash);
    }
}
