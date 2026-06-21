package com.hei.school.endpoint.rest.controller.Sale;

import com.hei.school.endpoint.rest.model.SaleRequest;
import com.hei.school.endpoint.rest.model.SaleResponse;
import com.hei.school.service.SaleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SaleController {

  private final SaleService saleService;

  @PostMapping("/sales")
  @ResponseStatus(HttpStatus.CREATED)
  public SaleResponse createSale(@Valid @RequestBody SaleRequest request) {
    return saleService.createSale(request);
  }
}
