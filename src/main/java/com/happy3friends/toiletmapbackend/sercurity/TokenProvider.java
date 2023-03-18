package com.happy3friends.toiletmapbackend.sercurity;

import com.happy3friends.toiletmapbackend.enums.PaymentTypeEnum;
import com.happy3friends.toiletmapbackend.enums.RoleEnum;
import com.happy3friends.toiletmapbackend.utils.DateTimeUtil;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
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

    private final String JWT_SECRET = "bONJQcpVJ/2DSkWbkxm0xFEA9w6cALzJRQaIIfMS/hbYeuKNob2eclbqCUVWHXudz+FsTCCUbFjXvdTgF/KhSQ==";

    private final long JWT_EXPIRATION = 604800000L;

    public String generateToken(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Date now = DateTimeUtil.getDateNow();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        if (customUserDetails.getRole().equals(RoleEnum.USER.getRoleName())) {
            return Jwts.builder()
                    .setSubject(String.valueOf(customUserDetails.getId()))
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                    .claim("phone", customUserDetails.getUsername())
                    .claim("fullName", customUserDetails.getFullName())
                    .claim("defaultPayment", PaymentTypeEnum.getByTypeString(customUserDetails.getDefaultPayment()))
                    .claim("role", customUserDetails.getRole())
                    .claim("authorities", customUserDetails.getAuthorities())
                    .compact();
        }

        return Jwts.builder()
                .setSubject(String.valueOf(customUserDetails.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .claim("username", customUserDetails.getUsername())
                .claim("role", customUserDetails.getRole())
                .claim("authorities", customUserDetails.getAuthorities())
                .compact();
    }

    public int getAccountIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String jwt = bearerToken.substring(7);
            if (StringUtils.hasText(jwt) && validateToken(jwt)) {
                return jwt;
            }
        }

        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
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
