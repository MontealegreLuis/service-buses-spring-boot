package com.montealegreluis.servicebusesspringboot.commands;

import com.montealegreluis.servicebuses.commandbus.CommandHandler;
import com.montealegreluis.servicebusesspringboot.repositories.EntityARepository;
import com.montealegreluis.servicebusesspringboot.repositories.EntityBRepository;

public final class DisableAccountAction implements CommandHandler<DisableAccountInput> {
  private final EntityARepository repositoryA;
  private final EntityBRepository repositoryB;
  private final RuntimeException exception;

  public DisableAccountAction(
      EntityARepository repositoryA, EntityBRepository repositoryB, RuntimeException exception) {
    this.repositoryA = repositoryA;
    this.repositoryB = repositoryB;
    this.exception = exception;
  }

  @Override
  public void execute(DisableAccountInput input) {
    repositoryA.save(input.entityA());
    repositoryB.save(input.entityB());

    if (exception != null) throw exception;
  }
}
