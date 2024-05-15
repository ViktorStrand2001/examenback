package com.example.examenback.controllers;

import com.example.examenback.config.jwt.JwtAuthenticationResponse;
import com.example.examenback.config.jwt.JwtTokenGenerator;
import com.example.examenback.models.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(JwtTokenGenerator jwtTokenGenerator, AuthenticationManager authenticationManager) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/debug")
    public ResponseEntity<LoginRequest> debugEndpoint(@RequestBody String requestBody) {
        // Print the received request body to the console for debugging
        System.out.println("Received request body: " + requestBody);

        // Return a simple response acknowledging the receipt of the request
        return ResponseEntity.ok().body(new LoginRequest("TEST", "TEST2"));
    }


    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        // Authenticate the user based on the credentials provided in the LoginRequest
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        // Generate JWT token using the authenticated Authentication object
        String token = jwtTokenGenerator.generateToken(authentication);

        // Return the token in the response
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }
}

