package com.movieflix.movieApi.auth.repositories;

import com.movieflix.movieApi.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository< User,Integer> {

    Optional<User> findByUsername(String username);
 }
