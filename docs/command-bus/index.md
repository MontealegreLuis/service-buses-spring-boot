# Command Bus

- [Configuration](#configuration)
- [Using the command bus in a controller](#using-the-command-bus-in-a-controller)

## Configuration

As commonly done in a Spring Boot application, we need to start by creating a `@Configuration` class.

```java
@Configuration
public class CommandBusConfiguration 
    implements WithCommandBus {}
```

`WithCommandBus` is an interface with default implementations (trait) for all the `@Bean`s you'll need to set up a command bus.

In the following sections we'll explain how to customize this configuration class to get your command bus up and running.

- [Command handler middleware](#command-handler-middleware)
- [Command logger middleware](#command-logger-middleware)
- [Domain Events logger middleware](#domain-events-logger-middleware)
- [Command error handler middleware](#command-error-handler-middleware)
- [Transaction middleware](#transaction-middleware)
- [Command and Query buses](#command-and-query-buses)
- [Command bus](#command-bus)

### Command handler middleware

We'll start by configuring the `CommandHandlerMiddleware`.
`WithCommandbus` trait only requires you to specify the package where your command handlers are.

```java
@Override
public String commandHandlersPackageName() {
  return "commands.package";
}
```

### Command logger middleware

To set up your [command logger middleware](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/command-bus/logging.md), you'll only need to specify the class name to be used by your logger instance.

```java
@Override
public Class<?> applicationClass() {
  return YourApplication.class;
}
```

### Domain Events logger middleware

To set up your [domain events logger middleware](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/command-bus/logging-events.md) you won't need to implement any additional method.

### Command error handler middleware

To configure your [command error handler](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/command-bus/error-handler.md) there's no need to implement any other method.

Unless you want more [specific configuration](https://github.com/MontealegreLuis/activity-feed#masking-sensitive-information) for your error handler, you can use the default `ObjectMapper` from Spring Boot to create your bean.

### Transaction middleware

This middleware provides a **transactional boundary** for your commands.

First you need to enable transaction management.

```java
@Configuration
@EnableTransactionManagement
public class CommandBusConfiguration {
  // ...
}
```

You'll also need to add a component scan filter so that the middleware gets initialized properly.

```java
@Configuration
@EnableTransactionManagement
@ComponentScan(
  includeFilters = {
    @ComponentScanFilter(
      type = FilterType.ASSIGNABLE_TYPE,
      classes = TransactionMiddleware.class)
  }
)
public class CommandBusConfiguration {
  // ...
}
```

### Command and Query buses

Your application will most likely have both, command and query handlers.
To configure both use the `WithServiceBuses` trait.

```java
@Configuration
public class ServiceBusesConfiguration
    implements WithServiceBuses {
  @Override
  public Class<?> applicationClass() {
    return YourApplication.class;
  }

  @Override
  public String queryHandlersPackageName() {
    return "queries.package";
  }

  public String commandHandlersPackageName() {
    return "commands.package";
  }
}
```

### Command bus

You can choose from the sections above what middleware to configure and add to your command bus.
The snippet below shows how to create a bus with all the middleware provided in this package.

```java
@Bean
@RequestScope
public CommandBus commandBus(
  TransactionMiddleware transaction
  CommandHandlerMiddleware commandHandler,
  CommandLoggerMiddleware logger,
  CommandErrorHandlerMiddleware errorHandler) {

  var middleware = List.of(
    errorHandler,
    transaction,
    logger, 
    commandHandler);
  
  return new MiddlewareCommandBus(middleware);
}
```

## Using the command bus in a controller

Let's suppose we have a command to enroll a client to paperless billing.
Let's suppose we have a command handler class `EnrollToPaperlessBillingAction` annotated with `@Service`.
And let's suppose that we have a command `EnrollToPaperlessBillingInput`.

Below is the snippet that shows one way to implement this command in a RESTful API.

```java
@RestController
public final class EnrollToPaperlessBillingController {
  private final CommandBus bus;

  public EnrollToPaperlessBillingController(CommandBus bus) {
    this.bus = bus;
  }

  @PutMapping("/v1/payments-preferences")
  public ResponseEntity<Object> enrollToPaperlessBilling(
    @Valid @RequestBody EnrollToPaperlessBillingValues request
  ) {
    final EnrollToPaperlessBillingInput input = request.input();
    bus.dispatch(input);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
```

In the example above `EnrollToPaperlessBillingValues` would contain your regular validation annotations and `input` is a factory for your command.

Given the configuration of your command bus, explained in the previous section, the following would be true for this and all commands in your application (provided that you are using all the middleware provided byt this package).

1. Your command would be found and executed automatically
2. It would run within a transaction
3. Its execution time would be logged, or,
4. In case of error it would log the exception information
