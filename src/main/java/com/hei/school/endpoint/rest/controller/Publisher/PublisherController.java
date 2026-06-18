package com.hei.school.endpoint.rest.controller.Publisher;

import com.hei.school.endpoint.rest.model.PublisherCreateRequest;
import com.hei.school.endpoint.rest.model.PublisherResponse;
import com.hei.school.service.PublisherService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publishers")
@AllArgsConstructor
public class PublisherController {

  private final PublisherService publisherService;

  @GetMapping
  public List<PublisherResponse> getAllPublishers() {
    return publisherService.getAll();
  }

  @GetMapping("/{id}")
  public PublisherResponse getPublisherById(@PathVariable UUID id) {
    return publisherService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PublisherResponse createPublisher(@Valid @RequestBody PublisherCreateRequest request) {
    return publisherService.create(request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePublisher(@PathVariable UUID id) {
    publisherService.delete(id);
  }
}
