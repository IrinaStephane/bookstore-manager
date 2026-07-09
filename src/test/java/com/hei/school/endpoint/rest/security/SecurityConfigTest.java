package com.hei.school.endpoint.rest.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.hei.school.conf.EnvConf;
import com.hei.school.conf.FacadeIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class SecurityConfigTest extends FacadeIT {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void should_reject_secured_endpoint_without_api_key() {
    var response = restTemplate.getForEntity("/api/books", String.class);

    assertUnauthorized(response);
  }

  @Test
  void should_reject_secured_endpoint_with_wrong_api_key() {
    var headers = headersWith().apiKey("wrong-key").build();
    var entity = new HttpEntity<>(null, headers);

    var response = restTemplate.exchange("/api/books", HttpMethod.GET, entity, String.class);

    assertUnauthorized(response);
  }

  @Test
  void should_accept_secured_endpoint_with_valid_api_key() {
    var headers = headersWith().apiKey(EnvConf.TEST_API_KEY).build();
    var entity = new HttpEntity<>(null, headers);

    var response = restTemplate.exchange("/api/books", HttpMethod.GET, entity, String.class);

    assertNotUnauthorized(response);
  }

  static HttpHeadersBuilder headersWith() {
    return new HttpHeadersBuilder();
  }

  static final class HttpHeadersBuilder {
    private String apiKey;

    HttpHeadersBuilder apiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    HttpHeaders build() {
      var headers = new HttpHeaders();
      headers.set("X-API-KEY", apiKey);
      return headers;
    }
  }

  private void assertUnauthorized(ResponseEntity<String> response) {
    assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode().value());
  }

  private void assertNotUnauthorized(ResponseEntity<String> response) {
    assertFalse(
        response.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED),
        "Expected response not to be 401 Unauthorized");
  }
}
