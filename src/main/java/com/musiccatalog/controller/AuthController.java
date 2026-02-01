package com.musiccatalog.controller;

import com.musiccatalog.service.account.TokenService;
import com.musiccatalog.dto.LoginRequest;
import com.musiccatalog.dto.LoginResponse;
import com.musiccatalog.dto.RefreshTokenRequest;
import com.musiccatalog.dto.RegisterRequest;
import com.musiccatalog.model.account.RefreshToken;
import com.musiccatalog.model.account.User;
import com.musiccatalog.repository.account.UserRepository;
import com.musiccatalog.service.account.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
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

        User user = (User) auth.getPrincipal();
        var accessToken = tokenService.generateToken((User) auth.getPrincipal());
        RefreshToken refreshToken = refreshTokenService.create(user);
        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken.getToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request){

        RefreshToken refreshToken = refreshTokenService.findByToken(request.token())
                .orElseThrow(() -> new RuntimeException("Refresh token não encontrado"));

        refreshTokenService.verifyExpiration(refreshToken);

        RefreshToken newRefreshToken = refreshTokenService.rotate(refreshToken);

        String newAccessToken = tokenService.generateToken(refreshToken.getUser());

        return ResponseEntity.ok(new LoginResponse(newAccessToken, newRefreshToken.getToken()));

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal User authenticatedUser,
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        RefreshToken token = refreshTokenService.findByToken(request.token())
                .orElseThrow(() -> new RuntimeException("Refresh token não encontrado"));
        
        if (!token.getUser().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("Você não tem permissão para revogar este token");
        }
        
        refreshTokenService.revokeToken(request.token());
        return ResponseEntity.noContent().build();
    }
}
