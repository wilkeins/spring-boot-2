package com.user.demo.service;

import com.user.demo.exceptions.CustomException;
import com.user.demo.model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
@Service
public class TokenService {
    private static final String KEY;

    static {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
            SecretKey secretKey = keyGen.generateKey();
            KEY = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error al generar la clave secreta", e);
        }
    }
    public String generateToken(Users users) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(users.getEmail())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 900000)) // Token válido por 15 minutos
                .signWith(Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public String validateAndParseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8)))
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (JwtException e) {
            throw new CustomException("Token inválido o expirado", HttpStatus.UNAUTHORIZED);
        }
    }
}

