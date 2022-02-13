package com.montealegreluis.servicebusesspringboot.querybus.factory;

import com.montealegreluis.servicebuses.querybus.Query;
import com.montealegreluis.servicebuses.querybus.QueryHandler;
import com.montealegreluis.servicebuses.querybus.Response;
import com.montealegreluis.servicebuses.querybus.factory.CannotCreateQueryHandler;
import com.montealegreluis.servicebuses.querybus.factory.QueryHandlerFactory;
import io.vavr.control.Try;
import org.springframework.context.ApplicationContext;

public final class ApplicationContextQueryHandlerFactory implements QueryHandlerFactory {
  private final ApplicationContext context;

  public ApplicationContextQueryHandlerFactory(ApplicationContext context) {
    this.context = context;
  }

  @Override
  public QueryHandler<? extends Query, ? extends Response> queryFromName(
      Class<? extends QueryHandler<? extends Query, ? extends Response>> queryHandlerClass)
      throws CannotCreateQueryHandler {
    return Try.of(() -> context.getBean(queryHandlerClass))
        .getOrElseThrow((e) -> CannotCreateQueryHandler.forQuery(queryHandlerClass, e));
  }
}
