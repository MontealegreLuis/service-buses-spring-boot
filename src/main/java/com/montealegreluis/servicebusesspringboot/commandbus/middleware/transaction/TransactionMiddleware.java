package com.montealegreluis.servicebusesspringboot.commandbus.middleware.transaction;

import com.montealegreluis.servicebuses.commandbus.Command;
import com.montealegreluis.servicebuses.commandbus.CommandHandler;
import com.montealegreluis.servicebuses.commandbus.middleware.CommandMiddleware;
import org.springframework.transaction.annotation.Transactional;

public class TransactionMiddleware implements CommandMiddleware {
  @Override
  @Transactional
  public void execute(Command command, CommandHandler<Command> next) {
    next.execute(command);
  }
}
