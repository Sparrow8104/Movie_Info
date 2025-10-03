package com.movieflix.movieApi.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @Column(nullable = false,length = 550)
    @NotBlank(message = "Please enter a refresh token")
    private String refreshToken;

    @Column(nullable = false)
    private Instant expiarationTime;

    @OneToOne
    private User user;
}
