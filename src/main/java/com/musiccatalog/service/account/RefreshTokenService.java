package com.musiccatalog.service.account;

import com.musiccatalog.model.account.RefreshToken;
import com.musiccatalog.model.account.User;
import com.musiccatalog.repository.account.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {this.refreshTokenRepository = refreshTokenRepository;}

    public RefreshToken create(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpirationDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRevoked(false);
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken){
        if(refreshToken.expired()){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expirado");
        }

        if(refreshToken.getRevoked()){
            throw new RuntimeException("Refresh token revogado");
        }

        return refreshToken;
    }

    @Transactional
    public void revokeToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }
}
