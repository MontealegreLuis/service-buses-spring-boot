package com.montealegreluis.servicebusesspringboot.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories("com.montealegreluis.servicebusesspringboot.repositories")
@EntityScan("com.montealegreluis.servicebusesspringboot.repositories")
public class TransactionConfiguration {}
