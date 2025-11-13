package com.movieflix.movieApi.auth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.SimpleTimeZone;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fpid;//forgot password id

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne
    private User user;
}
