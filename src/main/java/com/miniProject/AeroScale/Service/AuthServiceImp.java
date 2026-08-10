package com.miniProject.AeroScale.Service;

import com.miniProject.AeroScale.DTO.Request.RegisterRequest;
import com.miniProject.AeroScale.DTO.Response.RegisterResponse;
import com.miniProject.AeroScale.Entity.Buyer;
import com.miniProject.AeroScale.Entity.Role;
import com.miniProject.AeroScale.Entity.Seller;
import com.miniProject.AeroScale.Entity.Users;
import com.miniProject.AeroScale.Exception.UserAlreadyExistsException;
import com.miniProject.AeroScale.Repository.BuyerRepository;
import com.miniProject.AeroScale.Repository.SellerRepository;
import com.miniProject.AeroScale.Repository.UserRepository;
import com.miniProject.AeroScale.Security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService{

    private UserRepository userRepository;
    private BuyerRepository buyerRepository;
    private SellerRepository sellerRepository;
    private JwtUtils jwtUtils;
    private RefreshTokenService refreshTokenService;

    public RegisterResponse register(@Valid RegisterRequest registerRequest) {
        if(registerRequest.getRole().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("Admin accounts cannot be self-registered");
        }
        if (userRepository.existsByEmailIgnoreCase(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("An account with this email already exists");
        }
        if (userRepository.existsByPhoneNo(registerRequest.getPhoneNo())) {
            throw new UserAlreadyExistsException("An account with this phone number already exists");
        }


        Users user = Users.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .PhoneNo(registerRequest.getPhoneNo())
                .role(Role.BUYER)
                .build();

        userRepository.save(user);

        if(registerRequest.getRole().equals(Role.BUYER)) {
            Buyer buyer = Buyer.builder()
                    .users(user)
                    .fullName(registerRequest.getFullName())
                    .DateOfBirth(registerRequest.getDOB())
                    .build();

            buyerRepository.save(buyer);
        }else {
            Seller seller = Seller.builder()
                    .users(user)
                    .bussinessName(registerRequest.getFullName())
                    .bussinessAdress(registerRequest.getBussinessAdress())
                    .wareHousePinCode(registerRequest.getWareHousePinCode())
                    .DateOfBirth(registerRequest.getDOB())
                    .sellerVerificationStatus(Seller.SellerVerificationStatus.PENDING)
                    .build();

            sellerRepository.save(seller);
        }
        return generateTokenPair(user, "registration", null);

    }

    public RegisterResponse generateTokenPair(Users user, String deviceInfo,String userIp) {
        String accessToken = jwtUtils.generateAccessToken(user.getId(), user.getEmail(), user.getRole().toString());
        String refreshToken = refreshTokenService.generateRefreshToken(user,deviceInfo,userIp);

       return RegisterResponse.builder()
                .UUID(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .ExpiresInSec(jwtUtils.getAccessTokenExpirationMs()/1000)
                .build();
    }
}
