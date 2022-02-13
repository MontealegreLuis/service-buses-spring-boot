# Command Bus

- [Configuration](#configuration)
- [Using the command bus in a controller](#using-the-command-bus-in-a-controller)

## Configuration

As commonly done in a Spring Boot application, we need to start by creating a `@Configuration` class.

```java
@Configuration
public class CommandBusConfiguration {}
```

In the following sections we'll explain how to add `@Bean`s to this configuration class to get your command bus up and running.

- [Command handler middleware](#command-handler-middleware)
- [Command logger middleware](#command-logger-middleware)
- [Command error handler middleware](#command-error-handler-middleware)
- [Transaction middleware](#transaction-middleware)
- [Command bus](#command-bus)

### Command handler middleware

We'll start by configuring the `CommandHandlerMiddleware`.
We'll use Spring Boot's [ApplicationContext](https://docs.spring.io/spring-framework/docs/2.5.x/reference/beans.html#context-introduction) as a factory for command handlers.

```java
@Bean
public CommandHandlerMiddleware commandHandlerMiddleware(
  ApplicationContext context) {
  var factory = new ApplicationContextCommandHandlerFactory(context);
  var locator = new ReflectionsCommandHandlerLocator("commands.package");
  return new CommandHandlerMiddleware(locator, factory);
}
```

As shown in the snippet above `ApplicationContext` can be passed directly to your `@Bean` factory method.

### Command logger middleware

To set up your [command logger middleware](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/command-bus/logging.md), you'll need the following factories.

```java
@Bean 
public Clock clock() {
  return Clock.systemUTC();
}

@Bean
public Logger logger() {
  return LoggerFactory.getLogger(YourSpringBootApplication.class);
}

@Bean
public ActivityFeed activityFeed(Logger logeer) {
  return new ActivityFeed(logger);
}

@Bean
public CommandLoggerMiddleware commandLoggerMiddleware(
  ActivityFeed feed, Clock clock) {
  return new CommandLoggerMiddleware(feed, clock);
}
```

### Command error handler middleware

To configure your [command error handler](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/command-bus/error-handler.md) you'll need the following beans.

```java
@Bean
public CommandErrorHandlerMiddleware commandErrorHandlerMiddleware(
    ActivityFeed feed, ObjectMapper mapper) {
  var serializer = new ContextSerializer(mapper);

  return new CommandErrorHandlerMiddleware(feed, serializer);
}
```

Unless you want more [specific configuration](https://github.com/MontealegreLuis/activity-feed#masking-sensitive-information) for your error handler, you can pass the `ObjectMapper` to your factory as shown in the snippet above.

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

### Command bus

You can choose from the sections above what middleware to configure and add to your command bus.
The snippet below shows how to create a bus with all the middleware provided in this package.

```java
@Bean
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
    EnrollToPaperlessBillingInput input = request.input();
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
