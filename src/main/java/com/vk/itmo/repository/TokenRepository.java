package com.vk.itmo.repository;

import com.vk.itmo.data.TokenStore;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenRepository {

    private final TokenStore tokenStore;
    // TODO Можно унести в конфиг секретный ключи, дополнительно сделать конструктор который принимает *Config
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Autowired
    public TokenRepository(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public String generateToken(Long userId) {

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(key)
                .compact();
    }


    public String getToken(Long userId) {
        return tokenStore.getToken(userId);
    }

    public Long extractUserId(String token) {
        return Long.valueOf(
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject() // Извлекает `sub` клейм, где хранится userId
        );
    }


    public void saveToken(Long userId, String token) {
        tokenStore.saveToken(userId, token);
    }

}
