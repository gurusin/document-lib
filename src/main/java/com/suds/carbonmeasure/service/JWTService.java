package com.suds.carbonmeasure.service;

import io.jsonwebtoken.impl.TextCodec;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    private final String SECRET_KEY = "zX8yFg6hJ$Mnc!7U^4rRqD@8q1Wk2m3L"; // Change this to a secure key
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // Token expiration time (10 hours)

    public String generateToken(String username, long id) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username,id);
    }

    private String createToken(Map<String, Object> claims, String subject,long id) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, String uname) {
        final String username = extractUsername(token);
        return (username.equals(uname) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}

