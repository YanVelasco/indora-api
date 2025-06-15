package com.fiap.indora.configs.security.jwt;

import com.fiap.indora.configs.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class AuthenticationJwtFilter extends OncePerRequestFilter {

    Logger logger = LogManager.getLogger(AuthenticationJwtFilter.class);

    final JwtProvider jwtProvider;
    final UserDetailsServiceImpl userDetailsServiceImpl;

    public AuthenticationJwtFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtProvider = jwtProvider;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtStr = getTokenHeader(request);
            if (jwtStr != null && jwtProvider.validateJwt(jwtStr)) {
                String userId = jwtProvider.getSubjectJwt(jwtStr);
                UserDetails userDetails = userDetailsServiceImpl.loadUserById(UUID.fromString(userId));
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenHeader(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

}
