package com.wos.workorder.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String JWT_SECRET = "MySecretKeyForJWTTokenGenerationAndValidation12345678901234567890";
    private final SecretKey secretKey;

    public JwtAuthenticationFilter() {
        this.secretKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        String token = extractTokenFromRequest(request);

        if (token != null && validateToken(token)) {
            // parse claims once so we can extract subject and roles
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            // Support multiple claim names to be robust: "role", "roles", "authorities"
            Object roleClaim = claims.get("role");
            if (roleClaim == null) roleClaim = claims.get("roles");
            if (roleClaim == null) roleClaim = claims.get("authorities");

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            if (roleClaim instanceof String) {
                String rolesStr = (String) roleClaim;
                // allow comma-separated roles
                String[] parts = rolesStr.split(",");
                for (String part : parts) {
                    String r = part.trim();
                    if (!r.isEmpty()) {
                        if (!r.startsWith("ROLE_")) r = "ROLE_" + r;
                        authorities.add(new SimpleGrantedAuthority(r));
                    }
                }
            } else if (roleClaim instanceof Collection) {
                for (Object o : (Collection<?>) roleClaim) {
                    if (o != null) {
                        String r = o.toString().trim();
                        if (!r.isEmpty()) {
                            if (!r.startsWith("ROLE_")) r = "ROLE_" + r;
                            authorities.add(new SimpleGrantedAuthority(r));
                        }
                    }
                }
            }

            // Fallback to ROLE_USER if no role claim present
            if (authorities.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                    username, 
                    null, 
                    authorities
                );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Authentication set for user: " + username + " with authorities: " + authorities);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    private String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}