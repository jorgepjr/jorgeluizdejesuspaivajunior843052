package com.musiccatalog.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRateLimiter {

    private final Map<String, RequestInfo> requests = new ConcurrentHashMap<>();
    private final int maxRequests;
    private final long windowMs;

    public InMemoryRateLimiter() {
        this.maxRequests = 10;
        this.windowMs = 60_000;
    }

    private static class RequestInfo {
        int count;
        long resetTime;

        RequestInfo(int count, long resetTime) {
            this.count = count;
            this.resetTime = resetTime;
        }
    }

    public synchronized boolean isAllowed(HttpServletRequest request) {
        String key = request.getHeader("Authorization");
        if (key == null) key = request.getRemoteAddr();

        long now = Instant.now().toEpochMilli();
        RequestInfo info = requests.get(key);

        if (info == null || now > info.resetTime) {
            requests.put(key, new RequestInfo(1, now + windowMs));
            return true;
        }

        if (info.count < maxRequests) {
            info.count++;
            return true;
        }

        return false;
    }

    public synchronized long getRetryAfter(HttpServletRequest request) {
        String key = request.getHeader("Authorization");
        if (key == null) key = request.getRemoteAddr();
        RequestInfo info = requests.get(key);
        long now = Instant.now().toEpochMilli();
        return info != null ? Math.max(0, info.resetTime - now) / 1000 : 0;
    }

    public synchronized int getRemaining(HttpServletRequest request) {
        String key = request.getHeader("Authorization");
        if (key == null) key = request.getRemoteAddr();
        RequestInfo info = requests.get(key);
        return info != null ? Math.max(0, maxRequests - info.count) : maxRequests;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public synchronized void cleanup() {
        long now = Instant.now().toEpochMilli();
        requests.entrySet().removeIf(e -> e.getValue().resetTime < now);
    }
}
