package com.hei.school.endpoint.rest.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hei.school.endpoint.rest.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

class ApiKeyFilterTest {

  private static final String HEADER_NAME = "X-API-KEY";

  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  private String validApiKey;
  private ApiKeyFilter filter;
  private HttpServletRequest request;
  private HttpServletResponse response;
  private FilterChain filterChain;
  private StringWriter responseBody;

  @BeforeEach
  void setUp() throws Exception {
    validApiKey = "valid-api-key";
    filter = new ApiKeyFilter(validApiKey, objectMapper);
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    filterChain = mock(FilterChain.class);
    responseBody = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(responseBody));
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void should_filter_secured_endpoints() {
    var req = requestWith().uri("/api/books").build();

    assertFalse(filter.shouldNotFilter(req));
  }

  @Test
  void should_filter_api_books_with_id() {
    var req = requestWith().uri("/api/books/123").build();

    assertFalse(filter.shouldNotFilter(req));
  }

  @Test
  void should_reject_when_api_key_missing() throws ServletException, IOException {
    var req = requestWith().uri("/api/books").noHeader().build();

    filter.doFilterInternal(req, response, filterChain);

    verify(response).setStatus(401);
    verify(response).setContentType("application/json;charset=UTF-8");
    verify(filterChain, never()).doFilter(req, response);

    var error = objectMapper.readValue(responseBody.toString(), ErrorResponse.class);
    assertErrorMessage()
        .status(401)
        .error("Unauthorized")
        .message("Missing API key: provide it in the X-API-KEY header")
        .path("/api/books")
        .assertOn(error);
  }

  @Test
  void should_reject_when_api_key_is_wrong() throws ServletException, IOException {
    var req = requestWith().uri("/api/books").header("wrong-key").build();

    filter.doFilterInternal(req, response, filterChain);

    verify(response).setStatus(401);
    verify(response).setContentType("application/json;charset=UTF-8");
    verify(filterChain, never()).doFilter(req, response);

    var error = objectMapper.readValue(responseBody.toString(), ErrorResponse.class);
    assertErrorMessage()
        .status(401)
        .error("Unauthorized")
        .message("Invalid API key")
        .path("/api/books")
        .assertOn(error);
  }

  @Test
  void should_authenticate_and_continue_chain_when_api_key_valid()
      throws ServletException, IOException {
    var req = requestWith().uri("/api/books").header(validApiKey).build();

    filter.doFilterInternal(req, response, filterChain);

    verify(filterChain).doFilter(req, response);

    var authentication = SecurityContextHolder.getContext().getAuthentication();
    assertNotNull(authentication);
    assertEquals("api-key-client", authentication.getName());
    assertEquals(1, authentication.getAuthorities().size());
    assertEquals(
        "ROLE_API_CLIENT", authentication.getAuthorities().iterator().next().getAuthority());
  }

  @Test
  void should_allow_permitted_path_with_valid_key() throws ServletException, IOException {
    var req = requestWith().uri("/ping").header(validApiKey).build();

    filter.doFilterInternal(req, response, filterChain);

    verify(filterChain).doFilter(req, response);
  }

  private TestRequestBuilder requestWith() {
    return new TestRequestBuilder();
  }

  static final class TestRequestBuilder {
    private String uri;
    private String apiKeyValue;
    private boolean hasHeader;

    TestRequestBuilder uri(String uri) {
      this.uri = uri;
      return this;
    }

    TestRequestBuilder header(String value) {
      this.apiKeyValue = value;
      this.hasHeader = true;
      return this;
    }

    TestRequestBuilder noHeader() {
      this.hasHeader = false;
      return this;
    }

    HttpServletRequest build() {
      var req = mock(HttpServletRequest.class);
      when(req.getRequestURI()).thenReturn(uri);
      if (hasHeader) {
        when(req.getHeader(HEADER_NAME)).thenReturn(apiKeyValue);
      } else {
        when(req.getHeader(HEADER_NAME)).thenReturn(null);
      }
      return req;
    }
  }

  static ErrorMessageAssertionBuilder assertErrorMessage() {
    return new ErrorMessageAssertionBuilder();
  }

  static final class ErrorMessageAssertionBuilder {
    private int status;
    private String error;
    private String message;
    private String path;

    ErrorMessageAssertionBuilder status(int status) {
      this.status = status;
      return this;
    }

    ErrorMessageAssertionBuilder error(String error) {
      this.error = error;
      return this;
    }

    ErrorMessageAssertionBuilder message(String message) {
      this.message = message;
      return this;
    }

    ErrorMessageAssertionBuilder path(String path) {
      this.path = path;
      return this;
    }

    void assertOn(ErrorResponse actual) {
      assertEquals(status, actual.status());
      assertEquals(error, actual.error());
      assertEquals(message, actual.message());
      assertEquals(path, actual.path());
    }
  }
}
