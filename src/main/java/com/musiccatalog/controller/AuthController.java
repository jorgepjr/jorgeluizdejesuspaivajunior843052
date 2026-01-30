package com.musiccatalog.controller;

import com.musiccatalog.config.TokenService;
import com.musiccatalog.dto.LoginRequest;
import com.musiccatalog.dto.LoginResponse;
import com.musiccatalog.dto.RegisterRequest;
import com.musiccatalog.model.account.User;
import com.musiccatalog.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity register(@Valid @RequestBody RegisterRequest request) {
        if (this.userRepository.findByLogin(request.getUsername()) != null) return ResponseEntity.badRequest().build();

        String encriptPass = new BCryptPasswordEncoder().encode(request.getPassword());
        User newUser = new User(request.getUsername(), encriptPass, request.getRole());
        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
