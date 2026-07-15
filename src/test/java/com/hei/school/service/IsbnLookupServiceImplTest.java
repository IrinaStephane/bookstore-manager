package com.hei.school.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.hei.school.exception.ResourceNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class IsbnLookupServiceImplTest {

  @Mock private RestTemplate restTemplate;
  @InjectMocks private IsbnLookupServiceImpl isbnLookupService;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(isbnLookupService, "googleBooksApiKey", "");
  }

  @Test
  void lookupShouldReturnOpenLibraryData() {
    String isbn = "9781234567890";
    Map<String, Object> openLibraryResponse = new LinkedHashMap<>();
    openLibraryResponse.put("key", "/books/OL1M");
    openLibraryResponse.put("title", "Test Book");
    openLibraryResponse.put("number_of_pages", 300);
    openLibraryResponse.put("authors", List.of(Map.of("key", "/authors/OL1A")));

    Map<String, Object> authorResponse = Map.of("name", "Test Author");

    given(restTemplate.getForObject("https://openlibrary.org/isbn/" + isbn + ".json", Map.class))
        .willReturn(openLibraryResponse);
    given(restTemplate.getForObject("https://openlibrary.org/authors/OL1A.json", Map.class))
        .willReturn(authorResponse);

    Map<String, Object> result = isbnLookupService.lookup(isbn);

    assertTrue(result.containsKey("ISBN:" + isbn));
    @SuppressWarnings("unchecked")
    Map<String, Object> data = (Map<String, Object>) result.get("ISBN:" + isbn);
    assertEquals("Test Book", data.get("title"));
    assertEquals("/books/OL1M", data.get("key"));
    assertEquals("https://openlibrary.org/books/OL1M", data.get("url"));
    assertEquals(300, data.get("number_of_pages"));

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> authors = (List<Map<String, Object>>) data.get("authors");
    assertNotNull(authors);
    assertEquals(1, authors.size());
    assertEquals("Test Author", authors.get(0).get("name"));
    assertEquals("https://openlibrary.org/authors/OL1A", authors.get(0).get("url"));
  }

  @Test
  void lookupShouldStripNonDigitCharacters() {
    String isbn = "978-1-234-56789-0";
    String cleanIsbn = "9781234567890";

    Map<String, Object> openLibraryResponse = new LinkedHashMap<>();
    openLibraryResponse.put("key", "/books/OL1M");
    openLibraryResponse.put("title", "Test Book");
    openLibraryResponse.put("number_of_pages", 200);

    given(
            restTemplate.getForObject(
                "https://openlibrary.org/isbn/" + cleanIsbn + ".json", Map.class))
        .willReturn(openLibraryResponse);

    Map<String, Object> result = isbnLookupService.lookup(isbn);

    assertTrue(result.containsKey("ISBN:" + cleanIsbn));
  }

  @Test
  void lookupShouldFallbackToGoogleBooksWhenOpenLibraryNotFound() {
    String isbn = "9780000000000";

    given(restTemplate.getForObject("https://openlibrary.org/isbn/" + isbn + ".json", Map.class))
        .willThrow(new HttpClientErrorException(org.springframework.http.HttpStatus.NOT_FOUND));

    Map<String, Object> volumeInfo = new LinkedHashMap<>();
    volumeInfo.put("title", "Google Book");
    volumeInfo.put("pageCount", 150);
    volumeInfo.put("authors", List.of("G Author"));

    Map<String, Object> googleItem = Map.of("volumeInfo", volumeInfo);
    Map<String, Object> googleResponse = Map.of("items", List.of(googleItem));

    given(
            restTemplate.getForObject(
                eq("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn), eq(Map.class)))
        .willReturn(googleResponse);

    Map<String, Object> result = isbnLookupService.lookup(isbn);

    assertTrue(result.containsKey("ISBN:" + isbn));
    @SuppressWarnings("unchecked")
    Map<String, Object> data = (Map<String, Object>) result.get("ISBN:" + isbn);
    assertEquals("Google Book", data.get("title"));
    assertEquals(150, data.get("number_of_pages"));
  }

  @Test
  void lookupShouldThrowWhenBothApisFail() {
    String isbn = "9780000000000";

    given(restTemplate.getForObject("https://openlibrary.org/isbn/" + isbn + ".json", Map.class))
        .willThrow(new HttpClientErrorException(org.springframework.http.HttpStatus.NOT_FOUND));

    Map<String, Object> googleResponse = Map.of();

    given(
            restTemplate.getForObject(
                eq("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn), eq(Map.class)))
        .willReturn(googleResponse);

    assertThrows(ResourceNotFoundException.class, () -> isbnLookupService.lookup(isbn));
  }

  @Test
  void lookupShouldEnrichAuthorsWithUrlAndName() {
    String isbn = "9781234567890";
    Map<String, Object> openLibraryResponse = new LinkedHashMap<>();
    openLibraryResponse.put("key", "/books/OL1M");
    openLibraryResponse.put("title", "Multi Author Book");
    openLibraryResponse.put(
        "authors", List.of(Map.of("key", "/authors/OL1A"), Map.of("key", "/authors/OL2A")));

    given(restTemplate.getForObject("https://openlibrary.org/isbn/" + isbn + ".json", Map.class))
        .willReturn(openLibraryResponse);
    given(restTemplate.getForObject("https://openlibrary.org/authors/OL1A.json", Map.class))
        .willReturn(Map.of("name", "Author One"));
    given(restTemplate.getForObject("https://openlibrary.org/authors/OL2A.json", Map.class))
        .willReturn(Map.of("name", "Author Two"));

    Map<String, Object> result = isbnLookupService.lookup(isbn);

    @SuppressWarnings("unchecked")
    Map<String, Object> data = (Map<String, Object>) result.get("ISBN:" + isbn);
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> authors = (List<Map<String, Object>>) data.get("authors");
    assertEquals(2, authors.size());
    assertEquals("Author One", authors.get(0).get("name"));
    assertEquals("https://openlibrary.org/authors/OL1A", authors.get(0).get("url"));
    assertEquals("Author Two", authors.get(1).get("name"));
    assertEquals("https://openlibrary.org/authors/OL2A", authors.get(1).get("url"));
  }
}
