
package com.example.demo.ratelim;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimiter rateLimiter;

    public RateLimitFilter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !(
            "POST".equalsIgnoreCase(request.getMethod())
            && "/orders".equals(request.getServletPath())
        );
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String clientId = request.getRemoteAddr();

        RateLimiter.RateLimitResult result =
                rateLimiter.checkLimit(clientId);

        response.setHeader(
                "X-RateLimit-Limit",
                String.valueOf(result.getLimit()));

        response.setHeader(
                "X-RateLimit-Remaining",
                String.valueOf(result.getRemaining()));

        if (!result.isAllowed()) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(
                "{\"message\":\"Too many requests. Try again later.\"}"
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}
