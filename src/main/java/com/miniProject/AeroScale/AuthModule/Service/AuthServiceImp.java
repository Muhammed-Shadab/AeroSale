package com.miniProject.AeroScale.AuthModule.Service;

import com.miniProject.AeroScale.AuthModule.DTO.Request.LoginRequest;
import com.miniProject.AeroScale.AuthModule.DTO.Request.RefreshTokenRequest;
import com.miniProject.AeroScale.AuthModule.DTO.Request.RegisterRequest;
import com.miniProject.AeroScale.AuthModule.DTO.Response.RegisterResponse;
import com.miniProject.AeroScale.BuyerModule.Entity.Buyer;
import com.miniProject.AeroScale.AuthModule.Entity.Role;
import com.miniProject.AeroScale.SellerModule.Entity.Seller;
import com.miniProject.AeroScale.AuthModule.Entity.Users;
import com.miniProject.AeroScale.AuthModule.Exception.AccountLockedException;
import com.miniProject.AeroScale.AuthModule.Exception.InvalidCredentialException;
import com.miniProject.AeroScale.AuthModule.Exception.UserAlreadyExistsException;
import com.miniProject.AeroScale.BuyerModule.Repository.BuyerRepository;
import com.miniProject.AeroScale.SellerModule.Repository.SellerRepository;
import com.miniProject.AeroScale.AuthModule.Repository.UserRepository;
import com.miniProject.AeroScale.AuthModule.Security.JwtUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService{

    private static  final int MAX_FAILED_LOGIN_ATTEMPT_COUNT = 5;
    private static final int ACCCOUNT_LOCK_DURATION = 15;

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    private final BuyerRepository buyerRepository;


    public RegisterResponse register(RegisterRequest registerRequest) {
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
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNo(registerRequest.getPhoneNo())
                .role(registerRequest.getRole())
                .build();

        userRepository.save(user);


        if(registerRequest.getRole().equals(Role.BUYER)) {

            //In case of MicroService this will be done by Event Driven Approach
            Buyer buyer = Buyer.builder()
                    .id(user.getId())
                    .DateOfBirth(registerRequest.getDOB())
                    .fullName(registerRequest.getFullName())
                    .build();

            buyerRepository.save(buyer);

        }else {
            //System.out.println("hello from seller side");
            //In case of MicroService this will be done by Event Driven Approach
            Seller seller = Seller.builder()
                    .id(user.getId())
                    .bussinessName(registerRequest.getFullName())
                    .bussinessAdress(registerRequest.getBussinessAdress())
                    .wareHousePinCode(registerRequest.getWareHousePinCode())
                    .DateOfBirth(registerRequest.getDOB())
                    .sellerVerificationStatus(Seller.SellerVerificationStatus.PENDING)
                    .build();

            sellerRepository.save(seller);
            //System.out.println("over");
        }
        //System.out.println("fin");
        return generateTokenPair(user, "registration", null);

    }

    @Override
    public RegisterResponse login(LoginRequest loginRequest) {

        Users user = userRepository.findByEmailIgnoreCase(loginRequest.getEmail()).orElseThrow(() ->
                new InvalidCredentialException("Email does not exists"));

        if(!user.isAccountNotLocked()) {
            throw new AccountLockedException("Account is temporarily locked due to repeated failed login attempts. Try again after some times.");
        }

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            registerFailedAttempt(user);
            throw new InvalidCredentialException("Enter Valid password");
        }

        if(!user.isEnabled()) {
            throw new InvalidCredentialException("Account is disables");
        }

        registerSuccesfullLogin(user);

        return generateTokenPair(user, "registration", null);
    }

    @Override
    public RegisterResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        Users user = refreshTokenService.validateRefreshToken(refreshTokenRequest.getToken());
        return generateTokenPair(user, null,null);
    }

    @Override
    public void logout(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.revoke(refreshTokenRequest.getToken());
    }


    private void registerFailedAttempt(Users user) {
        user.setFailedLoginAttempt(user.getFailedLoginAttempt() + 1);

        if(user.getFailedLoginAttempt() >= MAX_FAILED_LOGIN_ATTEMPT_COUNT) {
            user.setAccountNotLocked(false);
            user.setAccountLockedUntil(Instant.now().plus(ACCCOUNT_LOCK_DURATION, ChronoUnit.MINUTES));
        }

        userRepository.save(user);
    }

    private void registerSuccesfullLogin(Users user) {
        user.setAccountNotLocked(true);
        user.setFailedLoginAttempt(0);
        userRepository.save(user);
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
