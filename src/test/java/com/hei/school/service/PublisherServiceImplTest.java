package com.hei.school.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.hei.school.endpoint.rest.mapper.PublisherMapper;
import com.hei.school.endpoint.rest.model.PublisherCreateRequest;
import com.hei.school.endpoint.rest.model.PublisherResponse;
import com.hei.school.entity.Publisher;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.repository.PublisherRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {

  @Mock private PublisherRepository publisherRepository;
  @Mock private PublisherMapper publisherMapper;
  @InjectMocks private PublisherServiceImpl publisherService;

  @Test
  void getAllShouldReturnAll() {
    var publisher = new Publisher();
    var response = PublisherResponse.builder().build();

    given(publisherRepository.findAll()).willReturn(List.of(publisher));
    given(publisherMapper.toResponse(publisher)).willReturn(response);

    var result = publisherService.getAll();

    assertEquals(1, result.size());
  }

  @Test
  void getByIdShouldReturn() {
    var id = UUID.randomUUID();
    var publisher = new Publisher();
    var response = PublisherResponse.builder().build();

    given(publisherRepository.findById(id)).willReturn(Optional.of(publisher));
    given(publisherMapper.toResponse(publisher)).willReturn(response);

    var result = publisherService.getById(id);

    assertNotNull(result);
  }

  @Test
  void getByIdShouldThrowWhenNotFound() {
    given(publisherRepository.findById(any())).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
        () -> publisherService.getById(UUID.randomUUID()));
  }

  @Test
  void createShouldPersistAndReturn() {
    var request = PublisherCreateRequest.builder().name("Penguin").build();
    var publisher = new Publisher();
    var saved = new Publisher();
    var response = PublisherResponse.builder().build();

    given(publisherMapper.toEntity(request)).willReturn(publisher);
    given(publisherRepository.save(publisher)).willReturn(saved);
    given(publisherMapper.toResponse(saved)).willReturn(response);

    var result = publisherService.create(request);

    assertNotNull(result);
    then(publisherRepository).should().save(publisher);
  }

  @Test
  void deleteShouldRemoveWhenExists() {
    var id = UUID.randomUUID();
    given(publisherRepository.existsById(id)).willReturn(true);

    publisherService.delete(id);

    then(publisherRepository).should().deleteById(id);
  }

  @Test
  void deleteShouldThrowWhenNotFound() {
    var id = UUID.randomUUID();
    given(publisherRepository.existsById(id)).willReturn(false);

    assertThrows(ResourceNotFoundException.class, () -> publisherService.delete(id));
  }
}
