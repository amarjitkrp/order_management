
package com.example.demo.ratelim;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import com.example.demo.ratelim.RateLimiter;

import org.springframework.stereotype.Component;

@Component
public class RateLimiter {

    private static final int MAX_REQUESTS = 10;
    private static final long WINDOW_MS = 60_000;

    private static class RequestCounter {
        long windowStart;
        AtomicInteger count;

        RequestCounter(long windowStart) {
            this.windowStart = windowStart;
            this.count = new AtomicInteger(0);
        }
    }

    public static class RateLimitResult {
        private final boolean allowed;
        private final int limit;
        private final int remaining;

        public RateLimitResult(
                boolean allowed, int limit, int remaining) {
            this.allowed = allowed;
            this.limit = limit;
            this.remaining = remaining;
        }

        public boolean isAllowed() {
            return allowed;
        }

        public int getLimit() {
            return limit;
        }

        public int getRemaining() {
            return remaining;
        }
    }

    private final ConcurrentHashMap<String, RequestCounter> counters =
            new ConcurrentHashMap<>();

    public RateLimitResult checkLimit(String userId) {
        long now = System.currentTimeMillis();

        RequestCounter counter = counters.compute(userId, (key, current) -> {
            if (current == null ||
                    now - current.windowStart >= WINDOW_MS) {
                return new RequestCounter(now);
            }
            return current;
        });

        int currentCount = counter.count.incrementAndGet();
        boolean allowed = currentCount <= MAX_REQUESTS;
        int remaining = Math.max(0, MAX_REQUESTS - currentCount);

        return new RateLimitResult(
                allowed, MAX_REQUESTS, remaining);
    }
}
