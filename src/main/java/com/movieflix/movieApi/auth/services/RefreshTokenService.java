package com.movieflix.movieApi.auth.services;


import com.movieflix.movieApi.auth.entities.RefreshToken;
import com.movieflix.movieApi.auth.entities.User;
import com.movieflix.movieApi.auth.repositories.RefreshTokenRepository;
import com.movieflix.movieApi.auth.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {


    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshtoken(String username){
        User user=userRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("User not found!"));
        RefreshToken refreshToken=user.getRefreshToken();

        if(refreshToken==null){
            long refreshTokenValidity=5*60*60*10000;
            refreshToken=RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiarationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();

            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;

    }

    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken refToken=refreshTokenRepository.findByRefreshtoken(refreshToken).
                orElseThrow(()-> new RuntimeException("Token not found"));
        if(refToken.getExpiarationTime().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh Token Expired!");

        }

        return refToken;
    }
}
