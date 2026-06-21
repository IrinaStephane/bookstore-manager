package com.hei.school.service;

import com.hei.school.endpoint.rest.model.ArrivalItemRequest;
import com.hei.school.endpoint.rest.model.ArrivalItemResponse;
import com.hei.school.endpoint.rest.model.ArrivalRequest;
import com.hei.school.endpoint.rest.model.ArrivalResponse;
import com.hei.school.entity.Arrival;
import com.hei.school.entity.ArrivalItem;
import com.hei.school.entity.BookEdition;
import com.hei.school.exception.ResourceNotFoundException;
import com.hei.school.repository.ArrivalRepository;
import com.hei.school.repository.BookEditionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArrivalServiceImpl implements ArrivalService {

  private final ArrivalRepository arrivalRepository;
  private final BookEditionRepository bookEditionRepository;

  @Transactional
  public ArrivalResponse createArrival(ArrivalRequest request) {
    Arrival arrival = new Arrival();
    arrival.setArrivalDate(request.getArrivalDate());
    arrival.setNotes(request.getNotes());

    List<ArrivalItem> arrivalItems = new ArrayList<>();
    double totalCost = 0;

    for (ArrivalItemRequest itemReq : request.getItems()) {
      BookEdition edition =
          bookEditionRepository
              .findById(itemReq.getEditionId())
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "Edition with id " + itemReq.getEditionId() + " not found"));

      edition.incrementStock(itemReq.getQuantity());
      bookEditionRepository.save(edition);

      ArrivalItem item = new ArrivalItem();
      item.setQuantity(itemReq.getQuantity());
      item.setUnitCost(itemReq.getUnitCost());
      item.setCondition(itemReq.getCondition());
      item.setEdition(edition);
      item.setArrival(arrival);

      arrivalItems.add(item);
      totalCost += item.getLineTotal();
    }

    arrival.setItems(arrivalItems);

    Arrival savedArrival = arrivalRepository.save(arrival);

    return toResponse(savedArrival, totalCost);
  }

  private ArrivalResponse toResponse(Arrival arrival, double totalCost) {
    List<ArrivalItemResponse> itemResponses =
        arrival.getItems().stream()
            .map(
                item ->
                    ArrivalItemResponse.builder()
                        .id(item.getId())
                        .quantity(item.getQuantity())
                        .unitCost(item.getUnitCost())
                        .lineTotal(item.getLineTotal())
                        .condition(item.getCondition())
                        .editionId(item.getEdition().getId())
                        .editionIsbn(item.getEdition().getIsbn())
                        .build())
            .toList();

    return ArrivalResponse.builder()
        .id(arrival.getId())
        .arrivalDate(arrival.getArrivalDate())
        .notes(arrival.getNotes())
        .totalCost(totalCost)
        .items(itemResponses)
        .build();
  }
}
