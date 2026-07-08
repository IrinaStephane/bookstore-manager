package com.hei.school.service;

import com.hei.school.endpoint.rest.model.GenreRevenueResponse;
import java.util.List;
import java.util.UUID;

public interface RevenueService {
  List<GenreRevenueResponse> getRevenueByGenre();

  GenreRevenueResponse getRevenueByGenre(UUID genreId);
}
