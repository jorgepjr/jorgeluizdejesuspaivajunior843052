package com.musiccatalog.service.account;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.musiccatalog.model.account.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("music-catalog")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateExpirationData())
                    .sign(algorithm);

        }catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao tentar gerar token", exception);
        }
    }

    public String validateToken(String token){

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("music-catalog")
                    .build()
                    .verify(token)
                    .getSubject();
        }
        catch (JWTVerificationException exception){
           return "";
        }
    }

    private Instant generateExpirationData(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-04:00"));
    }
}
