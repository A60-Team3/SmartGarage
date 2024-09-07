package org.example.smartgarage.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        boolean isAjaxRequest = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        if (requestUri.startsWith("/api") || isAjaxRequest) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            try (PrintWriter writer = response.getWriter()) {
                writer.write("{\"error\": \"Unauthorized\", \"message\": \"Please log in to access this resource.\"}");
            }
        } else {
            response.sendRedirect("api/garage/login");
        }
    }
}
