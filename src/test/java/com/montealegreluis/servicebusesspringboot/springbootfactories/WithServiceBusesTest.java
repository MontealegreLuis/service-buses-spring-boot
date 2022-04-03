package com.montealegreluis.servicebusesspringboot.springbootfactories;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montealegreluis.servicebuses.domainevents.EventBus;
import com.montealegreluis.servicebusesmiddleware.commandbus.middleware.error.CommandErrorHandlerMiddleware;
import com.montealegreluis.servicebusesmiddleware.commandbus.middleware.events.EventsLoggerMiddleware;
import com.montealegreluis.servicebusesmiddleware.commandbus.middleware.handler.CommandHandlerMiddleware;
import com.montealegreluis.servicebusesmiddleware.commandbus.middleware.logger.CommandLoggerMiddleware;
import com.montealegreluis.servicebusesmiddleware.querybus.middleware.error.QueryErrorHandlerMiddleware;
import com.montealegreluis.servicebusesmiddleware.querybus.middleware.handler.QueryHandlerMiddleware;
import com.montealegreluis.servicebusesmiddleware.querybus.middleware.logger.QueryLoggerMiddleware;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

final class WithServiceBusesTest {
  @Test
  void it_allows_configuring_a_query_bus() {
    var context = new AnnotationConfigApplicationContext(QueryBusConfiguration.class);

    assertDoesNotThrow(() -> context.getBean(QueryHandlerMiddleware.class));
    assertDoesNotThrow(() -> context.getBean(QueryLoggerMiddleware.class));
    assertDoesNotThrow(() -> context.getBean(QueryErrorHandlerMiddleware.class));
  }

  @Test
  void it_allows_configuring_a_command_bus() {
    var context = new AnnotationConfigApplicationContext(CommandBusConfiguration.class);

    assertDoesNotThrow(() -> context.getBean(CommandHandlerMiddleware.class));
    assertDoesNotThrow(() -> context.getBean(CommandLoggerMiddleware.class));
    assertDoesNotThrow(() -> context.getBean(EventsLoggerMiddleware.class));
    assertDoesNotThrow(() -> context.getBean(CommandErrorHandlerMiddleware.class));
    assertDoesNotThrow(() -> context.getBean(EventBus.class));
  }

  @Test
  void it_allows_configuration_both_command_and_query_buses() {
    var context = new AnnotationConfigApplicationContext(ServiceBusesConfiguration.class);

    assertDoesNotThrow(() -> context.getBean(QueryHandlerMiddleware.class));
    assertDoesNotThrow(() -> context.getBean(QueryLoggerMiddleware.class));
    assertDoesNotThrow(() -> context.getBean(QueryErrorHandlerMiddleware.class));
    assertDoesNotThrow(() -> context.getBean(CommandHandlerMiddleware.class));
    assertDoesNotThrow(() -> context.getBean(CommandLoggerMiddleware.class));
    assertDoesNotThrow(() -> context.getBean(EventsLoggerMiddleware.class));
    assertDoesNotThrow(() -> context.getBean(CommandErrorHandlerMiddleware.class));
  }

  private static class QueryBusConfiguration implements WithQueryBus {
    @Bean
    public ObjectMapper objectMapper() {
      return new ObjectMapper();
    }

    @Override
    public String queryHandlersPackageName() {
      return "com.montealegreluis.servicebuses.fakes.querybus";
    }

    @Override
    public Class<?> applicationClass() {
      return WithServiceBusesTest.class;
    }
  }

  private static class CommandBusConfiguration implements WithCommandBus {
    @Bean
    public ObjectMapper objectMapper() {
      return new ObjectMapper();
    }

    @Override
    public Class<?> applicationClass() {
      return WithServiceBusesTest.class;
    }

    @Override
    public String commandHandlersPackageName() {
      return "com.montealegreluis.servicebuses.fakes.commandbus";
    }
  }

  private static class ServiceBusesConfiguration implements WithServiceBuses {
    @Bean
    public ObjectMapper objectMapper() {
      return new ObjectMapper();
    }

    @Override
    public Class<?> applicationClass() {
      return WithServiceBusesTest.class;
    }

    @Override
    public String commandHandlersPackageName() {
      return "com.montealegreluis.servicebuses.fakes.commandbus";
    }

    @Override
    public String queryHandlersPackageName() {
      return "com.montealegreluis.servicebuses.fakes.querybus";
    }
  }
}
