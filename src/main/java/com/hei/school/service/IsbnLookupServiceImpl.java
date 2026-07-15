package com.hei.school.service;

import com.hei.school.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class IsbnLookupServiceImpl implements IsbnLookupService {

  private static final String OPENLIBRARY_URL = "https://openlibrary.org/isbn/%s.json";
  private static final String GOOGLE_BOOKS_URL =
      "https://www.googleapis.com/books/v1/volumes?q=isbn:%s";

  private final RestTemplate restTemplate;

  @Value("${google.books.api.key:}")
  private String googleBooksApiKey;

  @Override
  public Map<String, Object> lookup(String rawIsbn) {
    String isbn = rawIsbn.replaceAll("[^\\dX]", "");
    String isbnKey = "ISBN:" + isbn;

    Map<String, Object> bookData =
        tryOpenLibrary(isbn)
            .orElseGet(
                () -> {
                  try {
                    return tryGoogleBooks(isbn);
                  } catch (HttpClientErrorException e) {
                    if (e.getStatusCode().value() == 429) {
                      log.error("Google Books rate limit exceeded for ISBN {}", isbn);
                      throw new RuntimeException("Google Books API rate limit exceeded");
                    }
                    log.warn("Google Books API error for ISBN {}: {}", isbn, e.getMessage());
                    throw new ResourceNotFoundException("No book found for ISBN: " + isbn);
                  }
                });

    Map<String, Object> result = new LinkedHashMap<>();
    result.put(isbnKey, reorderFields(bookData));
    return result;
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> reorderFields(Map<String, Object> data) {
    String[] orderedKeys = {
      "url",
      "key",
      "title",
      "authors",
      "number_of_pages",
      "pagination",
      "weight",
      "identifiers",
      "isbn_10",
      "isbn_13",
      "publishers",
      "publish_date",
      "physical_format",
      "subtitle",
      "description",
      "notes",
      "subjects",
      "series",
      "covers",
      "source"
    };

    Map<String, Object> ordered = new LinkedHashMap<>(data.size());
    for (String key : orderedKeys) {
      if (data.containsKey(key)) {
        ordered.put(key, data.get(key));
      }
    }
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      ordered.putIfAbsent(entry.getKey(), entry.getValue());
    }
    return ordered;
  }

  private Optional<Map<String, Object>> tryOpenLibrary(String isbn) {
    try {
      String url = String.format(OPENLIBRARY_URL, isbn);
      Map<String, Object> response = restTemplate.getForObject(url, Map.class);
      if (response == null) {
        return Optional.empty();
      }
      log.info("OpenLibrary lookup succeeded for ISBN: {}", isbn);
      return Optional.of(normalizeOpenLibrary(response));
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().value() == 404) {
        log.info("OpenLibrary: ISBN {} not found", isbn);
        return Optional.empty();
      }
      throw e;
    }
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> normalizeOpenLibrary(Map<String, Object> data) {
    String key = (String) data.get("key");
    if (key != null) {
      data.putIfAbsent("url", "https://openlibrary.org" + key);
    }

    Object rawAuthors = data.get("authors");
    if (rawAuthors instanceof List) {
      List<Map<String, Object>> enriched = new ArrayList<>();
      for (Object item : (List<Object>) rawAuthors) {
        if (item instanceof Map) {
          Map<String, Object> authorEntry = (Map<String, Object>) item;
          String authorKey = (String) authorEntry.get("key");
          if (authorKey != null) {
            Map<String, String> authorInfo = fetchAuthorInfo(authorKey);
            Map<String, Object> enrichedAuthor = new java.util.HashMap<>();
            enrichedAuthor.put("url", "https://openlibrary.org" + authorKey);
            enrichedAuthor.put("name", authorInfo.getOrDefault("name", authorKey));
            enriched.add(enrichedAuthor);
          }
        }
      }
      data.put("authors", enriched);
    }

    return data;
  }

  @SuppressWarnings("unchecked")
  private Map<String, String> fetchAuthorInfo(String authorKey) {
    try {
      String url = "https://openlibrary.org" + authorKey + ".json";
      Map<String, Object> authorData = restTemplate.getForObject(url, Map.class);
      if (authorData != null) {
        String name = (String) authorData.get("name");
        if (name != null) {
          return Map.of("name", name);
        }
      }
    } catch (Exception e) {
      log.warn("Failed to fetch author info for {}: {}", authorKey, e.getMessage());
    }
    return Map.of("name", authorKey);
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> tryGoogleBooks(String isbn) {
    String url = String.format(GOOGLE_BOOKS_URL, isbn);
    if (!googleBooksApiKey.isBlank()) {
      url += "&key=" + googleBooksApiKey;
    }

    Map<String, Object> rawResponse = restTemplate.getForObject(url, Map.class);

    if (rawResponse == null) {
      throw new ResourceNotFoundException("No book found for ISBN: " + isbn);
    }

    List<Map<String, Object>> items = (List<Map<String, Object>>) rawResponse.get("items");
    if (items == null || items.isEmpty()) {
      throw new ResourceNotFoundException("No book found for ISBN: " + isbn);
    }

    Map<String, Object> volumeInfo = (Map<String, Object>) items.get(0).get("volumeInfo");
    if (volumeInfo == null) {
      throw new ResourceNotFoundException("No book found for ISBN: " + isbn);
    }

    log.info("Google Books lookup succeeded for ISBN: {}", isbn);
    return normalizeGoogleBooks(volumeInfo, isbn);
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> normalizeGoogleBooks(Map<String, Object> volumeInfo, String isbn) {
    volumeInfo.putIfAbsent("isbn", isbn);
    volumeInfo.putIfAbsent("source", "google_books");

    List<String> authorNames = (List<String>) volumeInfo.get("authors");
    if (authorNames != null) {
      List<Map<String, String>> authors =
          authorNames.stream().map(name -> Map.of("name", name)).toList();
      volumeInfo.put("authors", authors);
    }

    Object pageCount = volumeInfo.get("pageCount");
    if (pageCount != null) {
      volumeInfo.putIfAbsent("number_of_pages", pageCount);
    }

    Object isbn10 = volumeInfo.get("industryIdentifiers");
    if (isbn10 instanceof List) {
      volumeInfo.put("identifiers", Map.of("isbn_10", isbn10));
    }

    Object publisher = volumeInfo.get("publisher");
    if (publisher != null) {
      volumeInfo.putIfAbsent("publishers", List.of(publisher));
    }

    Object publishedDate = volumeInfo.get("publishedDate");
    if (publishedDate != null) {
      volumeInfo.putIfAbsent("publish_date", publishedDate);
    }

    Object description = volumeInfo.get("description");
    if (description != null) {
      volumeInfo.putIfAbsent("notes", description);
    }

    return volumeInfo;
  }
}
