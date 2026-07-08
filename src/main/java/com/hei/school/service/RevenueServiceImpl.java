package com.hei.school.service;

import com.hei.school.endpoint.rest.model.GenreRevenueResponse;
import com.hei.school.entity.Genre;
import com.hei.school.entity.SaleItem;
import com.hei.school.repository.SaleItemRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RevenueServiceImpl implements RevenueService {

  private final SaleItemRepository saleItemRepository;

  @Override
  public List<GenreRevenueResponse> getRevenueByGenre() {
    List<SaleItem> allItems = saleItemRepository.findAll();

    Map<Genre, RevenueAccumulator> revenueMap = new LinkedHashMap<>();

    for (SaleItem item : allItems) {
      Double lineTotal = item.getLineTotal();
      List<Genre> genres = item.getEdition().getBook().getGenres();

      for (Genre genre : genres) {
        revenueMap.computeIfAbsent(genre, k -> new RevenueAccumulator()).add(lineTotal, 1L);
      }
    }

    return revenueMap.entrySet().stream()
        .map(
            entry ->
                GenreRevenueResponse.builder()
                    .genreId(entry.getKey().getId())
                    .genreName(entry.getKey().getName())
                    .totalRevenue(entry.getValue().totalRevenue)
                    .saleCount(entry.getValue().saleCount)
                    .build())
        .toList();
  }

  private static class RevenueAccumulator {
    private Double totalRevenue = 0.0;
    private Long saleCount = 0L;

    void add(Double revenue, Long count) {
      totalRevenue += revenue;
      saleCount += count;
    }
  }
}
