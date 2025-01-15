
package com.vk.itmo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${security.csrf.enabled:false}")
    private boolean csrfEnabled;

    @Value("${security.session.creationPolicy:STATELESS}")
    private String sessionCreationPolicy;

    public boolean isCsrfEnabled() {
        return csrfEnabled;
    }

    public String getSessionCreationPolicy() {
        return sessionCreationPolicy;
    }
    @Value("${security.auth.header:Authorization}")
    private String authHeader;

    @Value("${security.auth.prefix:Bearer }")
    private String authPrefix;

    public String getAuthHeader() {
        return authHeader;
    }

    public String getAuthPrefix() {
        return authPrefix;
    }
}

