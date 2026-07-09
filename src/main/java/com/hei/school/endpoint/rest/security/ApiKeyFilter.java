package com.hei.school.endpoint.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.school.endpoint.rest.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class ApiKeyFilter extends OncePerRequestFilter {

  private static final String API_KEY_HEADER = "X-API-KEY";

  private final String apiKey;
  private final ObjectMapper objectMapper;

  public ApiKeyFilter(String apiKey, ObjectMapper objectMapper) {
    this.apiKey = apiKey;
    this.objectMapper = objectMapper;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.equals("/ping") || path.startsWith("/health/") || path.equals("/error");
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String headerKey = request.getHeader(API_KEY_HEADER);

    if (headerKey == null || !isValid(headerKey)) {
      log.warn(
          "Request rejected: {} from {}",
          headerKey == null ? "missing API key" : "invalid API key",
          request.getHeader("X-Forwarded-For") != null
              ? request.getHeader("X-Forwarded-For")
              : request.getRemoteAddr());

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json;charset=UTF-8");

      ErrorResponse body =
          new ErrorResponse(
              HttpStatus.UNAUTHORIZED.value(),
              HttpStatus.UNAUTHORIZED.getReasonPhrase(),
              headerKey == null
                  ? "Missing API key: provide it in the X-API-KEY header"
                  : "Invalid API key",
              LocalDateTime.now(),
              request.getRequestURI());
      objectMapper.writeValue(response.getWriter(), body);
      return;
    }

    var authentication =
        new UsernamePasswordAuthenticationToken(
            "api-key-client", null, List.of(new SimpleGrantedAuthority("ROLE_API_CLIENT")));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }

  private boolean isValid(String headerKey) {
    return MessageDigest.isEqual(
        headerKey.getBytes(StandardCharsets.UTF_8), apiKey.getBytes(StandardCharsets.UTF_8));
  }
}
