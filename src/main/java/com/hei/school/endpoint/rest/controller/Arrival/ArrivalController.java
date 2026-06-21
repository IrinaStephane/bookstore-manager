package com.hei.school.endpoint.rest.controller.Arrival;

import com.hei.school.endpoint.rest.model.ArrivalRequest;
import com.hei.school.endpoint.rest.model.ArrivalResponse;
import com.hei.school.service.ArrivalService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ArrivalController {

  private final ArrivalService arrivalService;

  @PostMapping("/arrivals")
  @ResponseStatus(HttpStatus.CREATED)
  public ArrivalResponse createArrival(@Valid @RequestBody ArrivalRequest request) {
    return arrivalService.createArrival(request);
  }
}
