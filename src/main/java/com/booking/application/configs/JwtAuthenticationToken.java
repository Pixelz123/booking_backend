package com.booking.application.configs;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.booking.application.entites.UserEntity;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String token;
    private final UserEntity principal;

    public JwtAuthenticationToken(String token, UserEntity principal) {
        super(null);
        this.token = token;
        this.principal = principal;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(UserEntity principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = null;
        this.principal = principal;
        setAuthenticated(true);
    }
    @Override
    public UserDetails getPrincipal() {
        return principal;
    }

    @Override
    public String getCredentials() {
        return token;
    }


}
