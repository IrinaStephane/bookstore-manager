package com.hei.school.endpoint.rest.model;

import java.util.UUID;

public record StockResponse(UUID editionId, String isbn, Integer quantityInStock) {}
