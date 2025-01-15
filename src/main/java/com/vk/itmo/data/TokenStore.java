package com.vk.itmo.data;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TokenStore {

    private final Map<Long, String> tokenMap = new ConcurrentHashMap<>();

    public void saveToken(Long userId, String token) {
        tokenMap.put(userId, token);
    }

    public String getToken(Long userId) {
        return tokenMap.get(userId);
    }
}
