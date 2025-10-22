package com.uas.auth.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

	// Hardcoded secret for demo (minimum 256 bits for HS256)
	private static final String JWT_SECRET = "MySecretKeyForJWTTokenGenerationAndValidation12345678901234567890";

	// Token expiration time: 24 hours in milliseconds
	private static final long JWT_EXPIRATION = 86400000L;

	private final SecretKey secretKey;

	public JwtTokenProvider() {
		this.secretKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Generates a JWT token from Spring Security Authentication object
	 * Adds a `roles` claim (list of roles without the ROLE_ prefix) so resource services
	 * can authorize based on roles.
	 * 
	 * @param authentication The authentication object containing user details
	 * @return Signed JWT token as a string
	 */
	public String generateToken(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

		// extract roles from authorities and strip the "ROLE_" prefix if present
		List<String> roles = userDetails.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.map(a -> a.startsWith("ROLE_") ? a.substring(5) : a)
			.collect(Collectors.toList());

		return Jwts.builder()
				.subject(userDetails.getUsername())
				.claim("roles", roles)
				.issuedAt(now)
				.expiration(expiryDate)
				.signWith(secretKey, Jwts.SIG.HS256)
				.compact();
	}

	/**
	 * Extracts username from JWT token
	 * 
	 * @param token JWT token string
	 * @return Username from the token
	 */
	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

		return claims.getSubject();
	}

	/**
	 * Validates the JWT token
	 * 
	 * @param token JWT token string
	 * @return true if token is valid, false otherwise
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
			return true;
		} catch (MalformedJwtException ex) {
			System.err.println("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			System.err.println("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			System.err.println("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			System.err.println("JWT claims string is empty");
		}
		return false;
	}
}