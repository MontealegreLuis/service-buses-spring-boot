package com.montealegreluis.servicebusesspringboot.springbootfactories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.activityfeed.ContextSerializer;
import com.montealegreluis.servicebuses.commandbus.locator.ReflectionsCommandHandlerLocator;
import com.montealegreluis.servicebuses.domainevents.DomainEventsCollector;
import com.montealegreluis.servicebusesmiddleware.commandbus.middleware.error.CommandErrorHandlerMiddleware;
import com.montealegreluis.servicebusesmiddleware.commandbus.middleware.events.EventsLoggerMiddleware;
import com.montealegreluis.servicebusesmiddleware.commandbus.middleware.handler.CommandHandlerMiddleware;
import com.montealegreluis.servicebusesmiddleware.commandbus.middleware.logger.CommandLoggerMiddleware;
import com.montealegreluis.servicebusesspringboot.commandbus.factory.ApplicationContextCommandHandlerFactory;
import java.time.Clock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public interface WithCommandBus
    extends WithActivityFeed, WithSystemClock, WithDomainEventsCollector {
  @Bean
  default CommandHandlerMiddleware commandHandlerMiddleware(ApplicationContext context) {
    var factory = new ApplicationContextCommandHandlerFactory(context);
    var locator = new ReflectionsCommandHandlerLocator(commandHandlersPackageName());
    return new CommandHandlerMiddleware(locator, factory);
  }

  String commandHandlersPackageName();

  @Bean
  default CommandLoggerMiddleware commandLoggerMiddleware(ActivityFeed feed, Clock clock) {
    return new CommandLoggerMiddleware(feed, clock);
  }

  @Bean
  default EventsLoggerMiddleware eventsLoggerMiddleware(
      DomainEventsCollector collector, ContextSerializer serializer, ActivityFeed feed) {
    return new EventsLoggerMiddleware(collector, feed, serializer);
  }

  @Bean
  default CommandErrorHandlerMiddleware commandErrorHandlerMiddleware(
      ActivityFeed feed, ObjectMapper mapper) {
    var serializer = new ContextSerializer(mapper);

    return new CommandErrorHandlerMiddleware(feed, serializer);
  }
}
