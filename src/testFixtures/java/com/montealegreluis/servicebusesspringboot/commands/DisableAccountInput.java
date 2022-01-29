package com.montealegreluis.servicebusesspringboot.commands;

import com.montealegreluis.servicebuses.commandbus.Command;
import com.montealegreluis.servicebusesspringboot.repositories.EntityA;
import com.montealegreluis.servicebusesspringboot.repositories.EntityB;

public final class DisableAccountInput implements Command {
  public EntityA entityA() {
    return new EntityA("value A");
  }

  public EntityB entityB() {
    return new EntityB("value B");
  }
}
