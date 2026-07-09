package com.hei.school.endpoint.rest.controller.Revenue;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hei.school.endpoint.rest.model.GenreRevenueResponse;
import com.hei.school.service.RevenueService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RevenueController.class)
class RevenueControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private RevenueService revenueService;

  @Test
  void getRevenueByGenreShouldReturnList() throws Exception {
    var response =
        GenreRevenueResponse.builder()
            .genreId(UUID.randomUUID())
            .genreName("Fiction")
            .totalRevenue(100.0)
            .build();

    given(revenueService.getRevenueByGenre()).willReturn(List.of(response));

    mockMvc
        .perform(get("/revenue/genres"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].genreName").value("Fiction"));
  }

  @Test
  void getRevenueByGenreIdShouldReturn() throws Exception {
    var id = UUID.randomUUID();
    var response =
        GenreRevenueResponse.builder().genreId(id).genreName("Fiction").totalRevenue(50.0).build();

    given(revenueService.getRevenueByGenre(id)).willReturn(response);

    mockMvc
        .perform(get("/revenue/genres/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalRevenue").value(50.0));
  }
}
