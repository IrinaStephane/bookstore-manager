package com.hei.school.service;

import com.hei.school.endpoint.rest.model.SaleItemRequest;
import com.hei.school.endpoint.rest.model.SaleItemResponse;
import com.hei.school.endpoint.rest.model.SaleRequest;
import com.hei.school.endpoint.rest.model.SaleResponse;
import com.hei.school.entity.BookEdition;
import com.hei.school.entity.Sale;
import com.hei.school.entity.SaleItem;
import com.hei.school.entity.enums.SaleStatus;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.repository.BookEditionRepository;
import com.hei.school.repository.SaleRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

  private final SaleRepository saleRepository;
  private final BookEditionRepository bookEditionRepository;

  @Transactional
  public SaleResponse createSale(SaleRequest request) {
    Sale sale = new Sale();
    sale.setUserId(request.getUserId());
    sale.setPaymentMethod(request.getPaymentMethod());
    sale.setStatus(SaleStatus.CONFIRMED);

    List<SaleItem> saleItems = new ArrayList<>();
    double totalAmount = 0;

    for (SaleItemRequest itemReq : request.getItems()) {
      BookEdition edition =
          bookEditionRepository
              .findById(itemReq.getEditionId())
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "Edition with id " + itemReq.getEditionId() + " not found"));

      if (edition.getQuantityInStock() < itemReq.getQuantity()) {
        throw new IllegalArgumentException(
            "Insufficient stock for edition " + itemReq.getEditionId());
      }

      edition.decrementStock(itemReq.getQuantity());
      bookEditionRepository.save(edition);

      SaleItem item = new SaleItem();
      item.setQuantity(itemReq.getQuantity());
      item.setUnitPrice(edition.getSellingPrice());
      item.setEdition(edition);
      item.setSale(sale);

      saleItems.add(item);
      totalAmount += item.getLineTotal();
    }

    sale.setTotalAmount(totalAmount);
    sale.setItems(saleItems);

    Sale savedSale = saleRepository.save(sale);

    return toResponse(savedSale);
  }

  private SaleResponse toResponse(Sale sale) {
    List<SaleItemResponse> itemResponses =
        sale.getItems().stream()
            .map(
                item ->
                    SaleItemResponse.builder()
                        .id(item.getId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .lineTotal(item.getLineTotal())
                        .editionId(item.getEdition().getId())
                        .editionIsbn(item.getEdition().getIsbn())
                        .build())
            .toList();

    return SaleResponse.builder()
        .id(sale.getId())
        .saleDate(sale.getSaleDate())
        .totalAmount(sale.getTotalAmount())
        .paymentMethod(sale.getPaymentMethod())
        .status(sale.getStatus())
        .userId(sale.getUserId())
        .items(itemResponses)
        .build();
  }
}
