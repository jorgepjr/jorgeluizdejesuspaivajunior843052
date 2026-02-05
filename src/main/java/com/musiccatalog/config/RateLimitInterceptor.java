package com.musiccatalog.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final InMemoryRateLimiter rateLimiter;

    public RateLimitInterceptor(InMemoryRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean allowed = rateLimiter.isAllowed(request);
        int remaining = rateLimiter.getRemaining(request);
        long retryAfter = rateLimiter.getRetryAfter(request);

        response.setHeader("X-RateLimit-Limit", String.valueOf(rateLimiter.getMaxRequests()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(remaining));
        response.setHeader("X-RateLimit-Retry-After-Seconds", String.valueOf(retryAfter));

        if (!allowed) {
            response.setStatus(429);
            response.getWriter().write("{"
                    + "\"error\":\"Too Many Requests\","
                    + "\"message\":\"Voca atingiu o limite de " + rateLimiter.getMaxRequests() + " requisicoes por minuto. "
                    + "Nao se preocupe, seu limite sera reiniciado em " + retryAfter + " segundos.\","
                    + "\"remaining\": " + remaining + ","
                    + "\"retryAfterSeconds\": " + retryAfter
                    + "}");
            return false;
        }
        return true;
    }
}
