package com.hei.school.service;

import com.hei.school.endpoint.rest.model.GenreRevenueResponse;
import java.util.List;

public interface RevenueService {
  List<GenreRevenueResponse> getRevenueByGenre();
}
