package com.hei.school.service;

import java.util.Map;

public interface IsbnLookupService {
  Map<String, Object> lookup(String isbn);
}
