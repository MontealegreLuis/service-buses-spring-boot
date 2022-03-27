package com.montealegreluis.servicebusesspringboot.springbootfactories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.activityfeed.ContextSerializer;
import com.montealegreluis.servicebuses.querybus.locator.ReflectionsQueryHandlerLocator;
import com.montealegreluis.servicebusesmiddleware.querybus.middleware.error.QueryErrorHandlerMiddleware;
import com.montealegreluis.servicebusesmiddleware.querybus.middleware.handler.QueryHandlerMiddleware;
import com.montealegreluis.servicebusesmiddleware.querybus.middleware.logger.QueryLoggerMiddleware;
import com.montealegreluis.servicebusesspringboot.querybus.factory.ApplicationContextQueryHandlerFactory;
import java.time.Clock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public interface WithQueryBus extends WithActivityFeed, WithSystemClock {
  @Bean
  default QueryHandlerMiddleware queryHandlerMiddleware(ApplicationContext context) {
    var factory = new ApplicationContextQueryHandlerFactory(context);
    var locator = new ReflectionsQueryHandlerLocator(queryHandlersPackageName());
    return new QueryHandlerMiddleware(locator, factory);
  }

  String queryHandlersPackageName();

  @Bean
  default QueryLoggerMiddleware queryLoggerMiddleware(ActivityFeed feed, Clock clock) {
    return new QueryLoggerMiddleware(feed, clock);
  }

  @Bean
  default QueryErrorHandlerMiddleware queryErrorHandlerMiddleware(
      ActivityFeed feed, ObjectMapper mapper) {
    var serializer = new ContextSerializer(mapper);

    return new QueryErrorHandlerMiddleware(feed, serializer);
  }
}
