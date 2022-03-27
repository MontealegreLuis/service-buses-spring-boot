package com.montealegreluis.servicebusesspringboot.springbootfactories;

import java.time.Clock;
import org.springframework.context.annotation.Bean;

public interface WithSystemClock {
  @Bean
  default Clock clock() {
    return Clock.systemUTC();
  }
}
