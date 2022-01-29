package com.montealegreluis.servicebusesspringboot.commandbus.middleware.transaction;

import static org.junit.jupiter.api.Assertions.*;

import com.montealegreluis.servicebuses.commandbus.CommandBus;
import com.montealegreluis.servicebuses.commandbus.MiddlewareCommandBus;
import com.montealegreluis.servicebuses.commandbus.middleware.handler.CommandHandlerMiddleware;
import com.montealegreluis.servicebuses.commandbus.middleware.handler.InMemoryCommandHandlerFactory;
import com.montealegreluis.servicebuses.commandbus.middleware.handler.ReflectionsCommandHandlerLocator;
import com.montealegreluis.servicebusesspringboot.commands.DisableAccountAction;
import com.montealegreluis.servicebusesspringboot.commands.DisableAccountInput;
import com.montealegreluis.servicebusesspringboot.config.TransactionConfiguration;
import com.montealegreluis.servicebusesspringboot.repositories.EntityARepository;
import com.montealegreluis.servicebusesspringboot.repositories.EntityBRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("test")
@ContextConfiguration(classes = {TransactionConfiguration.class})
@SpringBootTest(
    classes = {TransactionMiddleware.class, EntityARepository.class, EntityBRepository.class})
final class TransactionMiddlewareTest {
  @Test
  void it_completes_a_database_transaction_if_no_error_occurs() {
    var bus = commandBus(null);

    bus.dispatch(new DisableAccountInput());

    assertEquals(1, entityARepository.findAll().size());
    assertEquals(1, entityBRepository.findAll().size());
  }

  @Test
  void it_rolls_back_a_database_transaction_if_an_error_occurs() {
    var bus = commandBus(new RuntimeException("MySQL server is gone"));

    assertThrows(RuntimeException.class, () -> bus.dispatch(new DisableAccountInput()));
    assertEquals(0, entityARepository.findAll().size());
    assertEquals(0, entityBRepository.findAll().size());
  }

  private CommandBus commandBus(RuntimeException exception) {
    var locator =
        new ReflectionsCommandHandlerLocator("com.montealegreluis.servicebusesspringboot.commands");
    var factory = new InMemoryCommandHandlerFactory();
    factory.add(
        DisableAccountAction.class,
        new DisableAccountAction(entityARepository, entityBRepository, exception));
    var handler = new CommandHandlerMiddleware(locator, factory);

    return new MiddlewareCommandBus(List.of(transaction, handler));
  }

  @BeforeEach
  void let() {
    entityARepository.deleteAll();
    entityBRepository.deleteAll();
  }

  @Autowired EntityARepository entityARepository;
  @Autowired EntityBRepository entityBRepository;
  @Autowired TransactionMiddleware transaction;
}
