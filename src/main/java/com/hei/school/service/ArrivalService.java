package com.hei.school.service;

import com.hei.school.endpoint.rest.model.ArrivalRequest;
import com.hei.school.endpoint.rest.model.ArrivalResponse;

public interface ArrivalService {
  ArrivalResponse createArrival(ArrivalRequest request);
}
