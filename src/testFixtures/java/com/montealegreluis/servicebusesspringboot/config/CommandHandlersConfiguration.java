package com.montealegreluis.servicebusesspringboot.config;

import com.montealegreluis.servicebuses.fakes.commandbus.FakeCommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandHandlersConfiguration {
  @Bean
  public FakeCommandHandler fakeCommandHandler() {
    return new FakeCommandHandler();
  }
}
