package com.mamydinyah.api.security.key;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamydinyah.api.dto.ErrorResponse;
import com.mamydinyah.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api/auth/") ||
                requestURI.startsWith("/swagger") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/v2/api-docs") ||
                requestURI.startsWith("/swagger-resources") ||
                requestURI.startsWith("/configuration")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader("x-api-key");

        if (apiKey == null || apiKey.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                    new ErrorResponse("API-KEY is missing")
            ));
            return;
        }

        if (!userRepository.existsByApiKey(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                    new ErrorResponse("Invalid API-KEY")
            ));
            return;
        }


        filterChain.doFilter(request, response);
    }
}
