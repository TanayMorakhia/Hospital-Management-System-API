package com.hms.api.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.debug("Authorization header: {}", auth);
        
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            logger.debug("Extracted token: {}", token.substring(0, Math.min(20, token.length())) + "...");
            
            try {
                Claims claims = jwtUtil.parse(token);
                String subject = claims.getSubject();
                String role = claims.get("role", String.class);
                logger.debug("Parsed claims - subject: {}, role: {}", subject, role);
                
                var authToken = new UsernamePasswordAuthenticationToken(subject, null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role)));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("Authentication set for user: {} with role: {}", subject, role);
            } catch (Exception e) {
                logger.error("JWT parsing failed: {}", e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}


