package com.hei.school.endpoint.rest.controller.Sale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.school.endpoint.rest.model.SaleItemRequest;
import com.hei.school.endpoint.rest.model.SaleRequest;
import com.hei.school.endpoint.rest.model.SaleResponse;
import com.hei.school.entity.enums.PaymentMethod;
import com.hei.school.entity.enums.SaleStatus;
import com.hei.school.service.SaleService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SaleController.class)
class SaleControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private SaleService saleService;

  @Test
  void createSaleShouldReturn201() throws Exception {
    var request =
        SaleRequest.builder()
            .userId(UUID.randomUUID())
            .email("test@test.com")
            .paymentMethod(PaymentMethod.CARD)
            .items(
                List.of(SaleItemRequest.builder().editionId(UUID.randomUUID()).quantity(2).build()))
            .build();
    var response =
        SaleResponse.builder().id(UUID.randomUUID()).status(SaleStatus.CONFIRMED).build();

    given(saleService.createSale(any())).willReturn(response);

    mockMvc
        .perform(
            post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("CONFIRMED"));
  }

  @Test
  void createSaleShouldReturn400WhenInvalid() throws Exception {
    mockMvc
        .perform(
            post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(SaleRequest.builder().build())))
        .andExpect(status().isBadRequest());
  }
}
