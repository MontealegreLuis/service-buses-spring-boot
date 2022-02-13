package com.montealegreluis.servicebusesspringboot.config;

import com.montealegreluis.servicebuses.fakes.querybus.FakeQueryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryHandlersConfiguration {
  @Bean
  public FakeQueryHandler fakeQueryHandler() {
    return new FakeQueryHandler();
  }
}
