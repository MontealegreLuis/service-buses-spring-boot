package com.montealegreluis.servicebusesspringboot.springbootfactories;

import com.montealegreluis.servicebuses.domainevents.DomainEventsCollector;
import org.springframework.context.annotation.Bean;

public interface WithDomainEventsCollector {
  @Bean
  default DomainEventsCollector domainEventsCollector() {
    return new DomainEventsCollector();
  }
}
