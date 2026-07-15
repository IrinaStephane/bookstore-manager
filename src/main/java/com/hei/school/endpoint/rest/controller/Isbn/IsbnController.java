package com.hei.school.endpoint.rest.controller.Isbn;

import com.hei.school.service.IsbnLookupService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IsbnController {

  private final IsbnLookupService isbnLookupService;

  @GetMapping("/isbn/{isbn}")
  public Map<String, Object> lookupIsbn(@PathVariable String isbn) {
    return isbnLookupService.lookup(isbn);
  }
}
