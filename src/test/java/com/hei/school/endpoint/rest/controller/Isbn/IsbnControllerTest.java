package com.hei.school.endpoint.rest.controller.Isbn;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.service.IsbnLookupService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(IsbnController.class)
class IsbnControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private IsbnLookupService isbnLookupService;

  @Test
  void lookupIsbnShouldReturnBookData() throws Exception {
    Map<String, Object> authors =
        Map.of("url", "https://openlibrary.org/authors/OL1A", "name", "Test Author");
    Map<String, Object> bookData = new LinkedHashMap<>();
    bookData.put("url", "https://openlibrary.org/books/OL1M");
    bookData.put("key", "/books/OL1M");
    bookData.put("title", "Test Book");
    bookData.put("authors", List.of(authors));
    bookData.put("number_of_pages", 300);

    given(isbnLookupService.lookup("9781234567890"))
        .willReturn(Map.of("ISBN:9781234567890", bookData));

    mockMvc
        .perform(get("/isbn/9781234567890"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$['ISBN:9781234567890'].title").value("Test Book"))
        .andExpect(jsonPath("$['ISBN:9781234567890'].key").value("/books/OL1M"))
        .andExpect(
            jsonPath("$['ISBN:9781234567890'].url").value("https://openlibrary.org/books/OL1M"))
        .andExpect(jsonPath("$['ISBN:9781234567890'].authors[0].name").value("Test Author"))
        .andExpect(jsonPath("$['ISBN:9781234567890'].number_of_pages").value(300));
  }

  @Test
  void lookupIsbnShouldReturn404WhenNotFound() throws Exception {
    given(isbnLookupService.lookup("0000000000"))
        .willThrow(new ResourceNotFoundException("No book found for ISBN: 0000000000"));

    mockMvc.perform(get("/isbn/0000000000")).andExpect(status().isNotFound());
  }
}
