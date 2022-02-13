package com.montealegreluis.servicebusesspringboot.querybus.factory;

import com.montealegreluis.servicebuses.contracttests.querybus.factory.QueryHandlerFactoryTest;
import com.montealegreluis.servicebuses.fakes.querybus.FakeQueryHandler;
import com.montealegreluis.servicebuses.fakes.querybus.SpyQueryHandler;
import com.montealegreluis.servicebuses.querybus.Query;
import com.montealegreluis.servicebuses.querybus.QueryHandler;
import com.montealegreluis.servicebuses.querybus.Response;
import com.montealegreluis.servicebuses.querybus.factory.QueryHandlerFactory;
import com.montealegreluis.servicebusesspringboot.config.QueryHandlersConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

final class ApplicationContextQueryHandlerFactoryTest extends QueryHandlerFactoryTest {
  @Override
  protected Class<? extends QueryHandler<? extends Query, ? extends Response>> knownQueryName() {
    return FakeQueryHandler.class;
  }

  @Override
  protected Class<? extends QueryHandler<? extends Query, ? extends Response>> unknownQueryName() {
    return SpyQueryHandler.class;
  }

  @Override
  public QueryHandlerFactory factory() {
    var context = new AnnotationConfigApplicationContext(QueryHandlersConfiguration.class);
    return new ApplicationContextQueryHandlerFactory(context);
  }
}
