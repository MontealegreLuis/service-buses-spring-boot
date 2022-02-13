package com.montealegreluis.servicebusesspringboot.commandbus.middleware.handler;

import com.montealegreluis.servicebuses.commandbus.Command;
import com.montealegreluis.servicebuses.commandbus.CommandHandler;
import com.montealegreluis.servicebuses.commandbus.factory.CannotCreateCommandHandler;
import com.montealegreluis.servicebuses.commandbus.factory.CommandHandlerFactory;
import io.vavr.control.Try;
import org.springframework.context.ApplicationContext;

public final class ApplicationContextCommandHandlerFactory implements CommandHandlerFactory {
  private final ApplicationContext context;

  public ApplicationContextCommandHandlerFactory(ApplicationContext context) {
    this.context = context;
  }

  @Override
  public CommandHandler<? extends Command> commandFromName(
      Class<? extends CommandHandler<? extends Command>> commandHandlerClass)
      throws CannotCreateCommandHandler {
    return Try.of(() -> context.getBean(commandHandlerClass))
        .getOrElseThrow((e) -> CannotCreateCommandHandler.forCommand(commandHandlerClass, e));
  }
}
