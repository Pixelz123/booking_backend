package com.booking.application.services;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
     private final String SECRET_KEY = "jwt_hyper_secret_key_must_not_be_shared##@#";
    private final long EXPIRATION_TIME_MILI=360000000;

    public String createToken(Authentication authentication)
    {

       return Jwts
              .builder()
              .setSubject(authentication.getName())
              .claim("roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date (System.currentTimeMillis()+EXPIRATION_TIME_MILI))
              .signWith(getSecurityKey())
              .compact();
    }
    private Key getSecurityKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
     public boolean validateToken(String token) {
        String username = extractUsername(token);
        return (!isTokenExpired(token) && username != null && !username.isEmpty());
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecurityKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
