package com.vk.itmo.config;

import com.vk.itmo.data.TokenStore;
import com.vk.itmo.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenRepository tokenRepository;
    private final TokenStore tokenStore;
    private final AppConfig appConfig;

    @Autowired
    public JwtAuthenticationFilter(TokenRepository tokenRepository, TokenStore tokenStore, AppConfig appConfig) {
        this.tokenRepository = tokenRepository;
        this.tokenStore = tokenStore;
        this.appConfig = appConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(appConfig.getAuthHeader());
        final String jwt;
        final Long userId;

        if (authHeader == null || !authHeader.startsWith(appConfig.getAuthPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(appConfig.getAuthPrefix().length());
        userId = tokenRepository.extractUserId(jwt);

        String storedToken = tokenStore.getToken(userId);
        if (storedToken != null && storedToken.equals(jwt)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, null, List.of());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}
