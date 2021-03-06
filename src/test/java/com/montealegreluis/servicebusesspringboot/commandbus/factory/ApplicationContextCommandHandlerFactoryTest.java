package com.montealegreluis.servicebusesspringboot.commandbus.factory;

import com.montealegreluis.servicebuses.commandbus.Command;
import com.montealegreluis.servicebuses.commandbus.CommandHandler;
import com.montealegreluis.servicebuses.commandbus.factory.CommandHandlerFactory;
import com.montealegreluis.servicebuses.contracttests.commandbus.factory.CommandHandlerFactoryTest;
import com.montealegreluis.servicebuses.fakes.commandbus.FakeCommandHandler;
import com.montealegreluis.servicebuses.fakes.commandbus.SpyCommandHandler;
import com.montealegreluis.servicebusesspringboot.config.CommandHandlersConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

final class ApplicationContextCommandHandlerFactoryTest extends CommandHandlerFactoryTest {
  @Override
  protected Class<? extends CommandHandler<? extends Command>> knownCommandName() {
    return FakeCommandHandler.class;
  }

  @Override
  protected Class<? extends CommandHandler<? extends Command>> unknownCommandName() {
    return SpyCommandHandler.class;
  }

  @Override
  public CommandHandlerFactory factory() {
    var context = new AnnotationConfigApplicationContext(CommandHandlersConfiguration.class);
    return new ApplicationContextCommandHandlerFactory(context);
  }
}
