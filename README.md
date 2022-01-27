# Service Buses - Spring Boot

[![CI workflow](https://github.com/montealegreluis/services-buses-spring-boot/actions/workflows/ci.yml/badge.svg)](https://github.com/montealegreluis/services-buses-spring-boot/actions/workflows/ci.yml)
[![Release workflow](https://github.com/montealegreluis/services-buses-spring-boot/actions/workflows/release.yml/badge.svg)](https://github.com/montealegreluis/services-buses-spring-boot/actions/workflows/release.yml)
[![semantic-release: conventional-commits](https://img.shields.io/badge/semantic--release-conventionalcommits-e10079?logo=semantic-release)](https://github.com/semantic-release/semantic-release)

Spring Boot integration for [Command and Query buses](https://github.com/MontealegreLuis/service-buses).

## Installation

1. [Authenticating to GitHub Packages](https://github.com/MontealegreLuis/services-buses-spring-boot/blob/main/docs/installation/authentication.md)
2. [Maven](https://github.com/MontealegreLuis/services-buses-spring-boot/blob/main/docs/installation/maven.md)
3. [Gradle](https://github.com/MontealegreLuis/services-buses-spring-boot/blob/main/docs/installation/gradle.md)

## Usage

```java
@Configuration
public class CommandBusConfiguration {
  @Bean
  public CommandHandlerMiddleware commandHandlerMiddleware(
    ApplicationContext context) {
    var factory = new ApplicationContextCommandHandlerFactory(context);
    var locator = new ReflectionsCommandHandlerLocator("commands.package");
    return new CommandHandlerMiddleware(locator, factory);
  }
}
```
