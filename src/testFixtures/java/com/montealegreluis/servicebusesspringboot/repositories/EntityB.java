package com.montealegreluis.servicebusesspringboot.repositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EntityB {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String column;

  public EntityB() {}

  public EntityB(String column) {
    this.column = column;
  }
}
