package com.happy3friends.toiletmapbackend.sercurity;

import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import com.happy3friends.toiletmapbackend.utils.JwtUtil;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for generating and verifying JWT
 * The following utility class will be used for generating a JWT after a user logs in successfully,
 * and validating the JWT sent in the Authorization header of the requests
 * The utility class reads the JWT secret and expiration time from properties.
 */
@Component
public class TokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);

    public String generateToken(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Date now = DateTimeUtil.getDateNow();
        Date expiryDate = new Date(now.getTime() + JwtUtil.JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(String.valueOf(customUserDetails.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JwtUtil.JWT_SECRET)
                .claim("username", customUserDetails.getUsername())
                .claim("role", customUserDetails.getRole())
                .claim("authorities", customUserDetails.getAuthorities())
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JwtUtil.JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            LOGGER.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty.");
        }
        return false;
    }
}
