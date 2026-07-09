package com.hei.school.conf;

import org.springframework.test.context.DynamicPropertyRegistry;

public class EnvConf {

  public static final String TEST_API_KEY = "test-api-key";

  public void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("app.api-key", () -> TEST_API_KEY);
  }
}
