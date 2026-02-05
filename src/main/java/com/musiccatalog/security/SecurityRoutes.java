package com.musiccatalog.security;

public class SecurityRoutes {

    private SecurityRoutes(){}

    public static final String[] PUBLICO = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",

            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/refresh",

            "/actuator",
            "/actuator/health",
            "/actuator/health/liveness",
            "/actuator/health/readiness",
            "/actuator/info",
            "/actuator/metrics",
            "/actuator/metrics/**"
    };

    public static final String[] USER = {
            "/api/v1/artistas/**",
            "/api/v1/albuns/**"
    };

    public static final String[] ADMIN = {
            "/api/v1/artistas/**",
            "/api/v1/albuns/**",
            "/api/v1/capas/**"
    };
}