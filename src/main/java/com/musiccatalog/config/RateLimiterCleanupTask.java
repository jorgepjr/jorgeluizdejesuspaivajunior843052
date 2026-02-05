package com.musiccatalog.config;

import org.springframework.scheduling.annotation.Scheduled;

public class RateLimiterCleanupTask {
    private final InMemoryRateLimiter rateLimiter;

    public RateLimiterCleanupTask(InMemoryRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Scheduled(fixedRate = 60_000)
    public void cleanup() {
        rateLimiter.cleanup();
    }
}
