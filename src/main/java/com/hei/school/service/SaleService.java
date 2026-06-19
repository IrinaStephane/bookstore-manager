package com.hei.school.service;

import com.hei.school.endpoint.rest.model.SaleRequest;
import com.hei.school.endpoint.rest.model.SaleResponse;

public interface SaleService {
  SaleResponse createSale(SaleRequest request);
}
