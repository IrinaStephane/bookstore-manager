package com.hei.school.endpoint.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class ApiKeyFilter extends OncePerRequestFilter {

  private final String apiKey;
  private final ObjectMapper objectMapper;

  public ApiKeyFilter(
      @Value("${app.api-key}") String apiKey,
      ObjectMapper objectMapper) {
    this.apiKey = apiKey;
    this.objectMapper = objectMapper;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.equals("/ping")
        || path.startsWith("/health/")
        || path.startsWith("/swagger-ui/")
        || path.startsWith("/v3/api-docs")
        || path.startsWith("/actuator/")
        || path.equals("/doc/api.yml")
        || path.equals("/error");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    String headerKey = request.getHeader("X-API-KEY");

    if (headerKey == null || !headerKey.equals(apiKey)) {
      log.warn("Request rejected: missing or invalid API key from {}", request.getRemoteAddr());
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json;charset=UTF-8");

      Map<String, Object> body = Map.of(
          "status", 401,
          "error", "Unauthorized",
          "message", headerKey == null
              ? "Missing API key: provide it in the X-API-KEY header"
              : "Invalid API key",
          "timestamp", LocalDateTime.now().toString(),
          "path", request.getRequestURI()
      );
      objectMapper.writeValue(response.getWriter(), body);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
