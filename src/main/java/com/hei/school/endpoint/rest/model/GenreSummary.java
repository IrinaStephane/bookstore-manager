package com.hei.school.endpoint.rest.model;

import com.hei.school.PojaGenerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PojaGenerated
public class GenreSummary {
    private Long id;
    private String name;
}