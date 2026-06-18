package com.hei.school.service;

import com.hei.school.endpoint.rest.mapper.PublisherMapper;
import com.hei.school.endpoint.rest.model.PublisherCreateRequest;
import com.hei.school.endpoint.rest.model.PublisherResponse;
import com.hei.school.entity.Publisher;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.repository.PublisherRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

  private final PublisherRepository publisherRepository;
  private final PublisherMapper publisherMapper;

  @Override
  public List<PublisherResponse> getAll() {
    return publisherRepository.findAll().stream().map(publisherMapper::toResponse).toList();
  }

  @Override
  public PublisherResponse getById(UUID id) {
    return publisherMapper.toResponse(findOrThrow(id));
  }

  @Override
  @Transactional
  public PublisherResponse create(PublisherCreateRequest request) {
    Publisher publisher = publisherMapper.toEntity(request);
    return publisherMapper.toResponse(publisherRepository.save(publisher));
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    if (!publisherRepository.existsById(id)) {
      throw new ResourceNotFoundException("Publisher with id " + id + " was not found");
    }
    publisherRepository.deleteById(id);
  }

  private Publisher findOrThrow(UUID id) {
    return publisherRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Publisher with id " + id + " was not found"));
  }
}
