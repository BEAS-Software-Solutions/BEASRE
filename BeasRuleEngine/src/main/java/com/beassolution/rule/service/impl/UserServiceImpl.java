package com.beassolution.rule.service.impl;

import com.beassolution.rule.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (Objects.nonNull(context)) {
            Authentication auth = context.getAuthentication();
            if (Objects.nonNull(auth)) {
                Object principle = auth.getPrincipal();
                if (Objects.nonNull(principle) && principle instanceof Jwt jwt) {
                    return jwt.getClaimAsString("preferred_username");
                }
            }
        }
        return "Anonymous";
    }
}
