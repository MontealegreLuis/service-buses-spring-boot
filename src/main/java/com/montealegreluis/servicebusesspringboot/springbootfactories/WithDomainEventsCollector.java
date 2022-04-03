package com.montealegreluis.servicebusesspringboot.springbootfactories;

import com.montealegreluis.servicebuses.domainevents.CollectorEventBus;
import com.montealegreluis.servicebuses.domainevents.DomainEventsCollector;
import com.montealegreluis.servicebuses.domainevents.EventBus;
import java.util.List;
import org.springframework.context.annotation.Bean;

public interface WithDomainEventsCollector {
  @Bean
  default DomainEventsCollector domainEventsCollector() {
    return new DomainEventsCollector();
  }

  @Bean
  default EventBus eventBus() {
    return new CollectorEventBus(List.of(new DomainEventsCollector()));
  }
}
