package com.hei.school.endpoint.rest.controller.Revenue;

import com.hei.school.endpoint.rest.model.GenreRevenueResponse;
import com.hei.school.service.RevenueService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/revenue")
@AllArgsConstructor
public class RevenueController {

  private final RevenueService revenueService;

  @GetMapping("/genres")
  public List<GenreRevenueResponse> getRevenueByGenre() {
    return revenueService.getRevenueByGenre();
  }
}
