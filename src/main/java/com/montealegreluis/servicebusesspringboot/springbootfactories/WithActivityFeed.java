package com.montealegreluis.servicebusesspringboot.springbootfactories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.activityfeed.ContextSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

public interface WithActivityFeed {
  @Bean
  default ContextSerializer contextSerializer(ObjectMapper mapper) {
    return new ContextSerializer(mapper);
  }

  @Bean
  default Logger logger() {
    return LoggerFactory.getLogger(applicationClass());
  }

  @Bean
  default ActivityFeed activityFeed(Logger logger) {
    return new ActivityFeed(logger);
  }

  Class<?> applicationClass();
}
