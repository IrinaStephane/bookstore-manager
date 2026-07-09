package com.hei.school.endpoint.rest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${app.api-key}")
  private String apiKey;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, ObjectMapper objectMapper)
          throws Exception {
    http.csrf(csrf -> csrf.disable())
            .sessionManagement(
                    session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(
                    auth ->
                            auth.requestMatchers("/ping", "/health/**", "/error")
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated())
            .addFilterBefore(
                    new ApiKeyFilter(apiKey, objectMapper), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}