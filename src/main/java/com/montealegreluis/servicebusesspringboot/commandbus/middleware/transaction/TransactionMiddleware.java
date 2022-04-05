package com.montealegreluis.servicebusesspringboot.commandbus.middleware.transaction;

import com.montealegreluis.servicebuses.ActionException;
import com.montealegreluis.servicebuses.commandbus.Command;
import com.montealegreluis.servicebuses.commandbus.CommandHandler;
import com.montealegreluis.servicebusesmiddleware.commandbus.middleware.CommandMiddleware;
import org.springframework.transaction.annotation.Transactional;

public class TransactionMiddleware implements CommandMiddleware {
  @Override
  @Transactional(rollbackFor = Throwable.class)
  public void execute(Command command, CommandHandler<Command> next) throws ActionException {
    next.execute(command);
  }
}
