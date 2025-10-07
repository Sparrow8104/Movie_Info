package com.movieflix.movieApi.controller;

import com.movieflix.movieApi.auth.utils.AuthResponse;
import com.movieflix.movieApi.auth.utils.RegisterRequest;
import jakarta.security.auth.message.AuthException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest){
       return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
