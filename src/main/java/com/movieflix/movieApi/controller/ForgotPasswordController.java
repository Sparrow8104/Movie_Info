package com.movieflix.movieApi.controller;

import com.movieflix.movieApi.auth.entities.ForgotPassword;
import com.movieflix.movieApi.auth.entities.User;
import com.movieflix.movieApi.auth.repositories.ForgotPasswordRepository;
import com.movieflix.movieApi.auth.repositories.UserRepository;
import com.movieflix.movieApi.auth.services.EmailService;
import com.movieflix.movieApi.dto.MailBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {


    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
    }

    //to verify emailId
    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email){
        User user=userRepository.findByEmail(email).
                orElseThrow(()->new UsernameNotFoundException("User not found!"));

        int otp=otpGenerator();

        MailBody mailBody=MailBody.builder()
                .to(email)
                .subject("This is the otp for the forgot password request"+otp)
                .text("OTP for forgot password request")
                .build();

        ForgotPassword fp= ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis()+15*1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);

        forgotPasswordRepository.save(fp);
        return ResponseEntity.ok("Email sent for verification!");

    }

    @PostMapping("verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp,@PathVariable String email){
        User user=userRepository.findByEmail(email).
                orElseThrow(()->new UsernameNotFoundException("User not found!"));

        ForgotPassword fp=
                forgotPasswordRepository.findByOtpAndUser(otp,user)
                        .orElseThrow(()->new RuntimeException("Invalid otp for email"+email));

        if(fp.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(fp.getFpid());
            return new ResponseEntity<>("Otp has expired", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("Otp is verified");

    }

    private Integer otpGenerator(){
        Random random=new Random();
        return random.nextInt(100_000,999_999);
    }
}
