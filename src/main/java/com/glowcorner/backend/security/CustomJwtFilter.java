package com.glowcorner.backend.security;

import com.glowcorner.backend.entity.mongoDB.User;
import com.glowcorner.backend.repository.UserRepository;
import com.glowcorner.backend.utils.JwtUtilHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class CustomJwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtilHelper jwtUtilHelper;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || "/auth/login".equals(path) || "/auth/signup".equals(path)
                || "/auth/register".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromHeader(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtUtilHelper.verifyToken(token)) {
            String email = jwtUtilHelper.getUsernameFromToken(token);

            // Check if the email exists in the user table (for admins)
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email, null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.get().getRole().name())));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;
            }

        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
