package com.booking.application.configs;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtAuthenticatorProvider jwtAuthProvider;

    public JwtAuthFilter(JwtAuthenticatorProvider jwtAuthProvider) {
        this.jwtAuthProvider = jwtAuthProvider;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/auth/") || path.startsWith("/public/");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        System.out.println("header for auth "+header+"\n");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Authentication auth = jwtAuthProvider.authenticate(new JwtAuthenticationToken(token));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ex) {
                throw new ServletException("Authentication Error\n");
            }
        } else {
            throw new IOException("Authorization header missing for "+request.getRequestURI()+"\n");
        }

        filterChain.doFilter(request, response);
    }
}
