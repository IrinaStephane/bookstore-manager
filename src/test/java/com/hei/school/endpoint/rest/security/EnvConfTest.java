package com.hei.school.endpoint.rest.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hei.school.conf.EnvConf;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;

class EnvConfTest {

  @Test
  void should_register_app_api_key_property() {
    var envConf = new EnvConf();
    var registry = new MemoryPropertyRegistry();

    envConf.configureProperties(registry);

    assertProperty().name("app.api-key").value(EnvConf.TEST_API_KEY).assertOn(registry);
  }

  static PropertyAssertionBuilder assertProperty() {
    return new PropertyAssertionBuilder();
  }

  static final class PropertyAssertionBuilder {
    private String expectedName;
    private String expectedValue;

    PropertyAssertionBuilder name(String expectedName) {
      this.expectedName = expectedName;
      return this;
    }

    PropertyAssertionBuilder value(String expectedValue) {
      this.expectedValue = expectedValue;
      return this;
    }

    void assertOn(DynamicPropertyRegistry registry) {
      var memRegistry = (MemoryPropertyRegistry) registry;
      assertEquals(expectedValue, memRegistry.getProperty(expectedName));
    }
  }

  static final class MemoryPropertyRegistry implements DynamicPropertyRegistry {
    private final Map<String, Object> properties = new HashMap<>();

    @Override
    public void add(String name, Supplier<Object> valueSupplier) {
      properties.put(name, valueSupplier.get());
    }

    public String getProperty(String name) {
      Object value = properties.get(name);
      return value != null ? (String) value : null;
    }
  }
}
