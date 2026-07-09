package com.hei.school.endpoint.rest.controller.Arrival;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.school.endpoint.rest.model.ArrivalItemRequest;
import com.hei.school.endpoint.rest.model.ArrivalRequest;
import com.hei.school.endpoint.rest.model.ArrivalResponse;
import com.hei.school.service.ArrivalService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArrivalController.class)
class ArrivalControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private ArrivalService arrivalService;

  @Test
  void createArrivalShouldReturn201() throws Exception {
    var request =
        ArrivalRequest.builder()
            .arrivalDate(LocalDate.now())
            .items(
                List.of(
                    ArrivalItemRequest.builder()
                        .editionId(UUID.randomUUID())
                        .quantity(10)
                        .unitCost(5.0)
                        .build()))
            .build();
    var response = ArrivalResponse.builder().id(UUID.randomUUID()).totalCost(50.0).build();

    given(arrivalService.createArrival(any())).willReturn(response);

    mockMvc
        .perform(
            post("/arrivals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.totalCost").value(50.0));
  }

  @Test
  void createArrivalShouldReturn400WhenInvalid() throws Exception {
    mockMvc
        .perform(
            post("/arrivals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ArrivalRequest.builder().build())))
        .andExpect(status().isBadRequest());
  }
}
