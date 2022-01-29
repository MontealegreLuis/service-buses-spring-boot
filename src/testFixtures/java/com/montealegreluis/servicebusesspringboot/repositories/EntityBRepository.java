package com.montealegreluis.servicebusesspringboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityBRepository extends JpaRepository<EntityB, Long> {}
