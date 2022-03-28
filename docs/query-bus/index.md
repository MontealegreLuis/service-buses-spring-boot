# Query Bus

- [Configuration](#configuration)
- [Using the query bus in a controller](#using-the-query-bus-in-a-controller)

## Configuration

As commonly done in a Spring Boot application, we need to start by creating a `@Configuration` class.

```java
@Configuration
public class QueryBusConfiguration
    implements WithQueryBus {}
```

In the following sections we'll explain how to customize this configuration class to get your query bus up and running.

- [Query handler middleware](#query-handler-middleware)
- [Query logger middleware](#query-logger-middleware)
- [Query error handler middleware](#query-error-handler-middleware)
- [Query and Command buses](#query-and-command-buses)
- [Query bus](#query-bus)

### Query handler middleware

We'll start by configuring the `QueryHandlerMiddleware`.
`WithQuerybus` trait only requires you to specify the package where your query handlers are.

```java
@Override
public String queryHandlersPackageName() {
  return "queries.package";
}
```

### Query logger middleware

To set up your [query logger middleware](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/query-bus/logging.md),  you'll only need to specify the class name to be used by your logger instance.

```java
@Override
public Class<?> applicationClass() {
  return YourApplication.class;
}
```

### Query error handler middleware

To configure your [query error handler](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/query-bus/error-handler.md) you won't need to implement any additional method.

Unless you want more [specific configuration](https://github.com/MontealegreLuis/activity-feed#masking-sensitive-information) for your error handler, you can use the default `ObjectMapper` from Spring Boot to create your bean.

### Query and Command buses

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

### Query bus

You can choose from the sections above what middleware to configure and add to your query bus.
The snippet below shows how to create a bus with all the middleware provided in this package.

```java
@Bean
@RequestScope
public QueryBus queryBus(
  QueryHandlerMiddleware queryHandler,
  QueryLoggerMiddleware logger,
  QueryErrorHandlerMiddleware errorHandler) {

  var middleware = List.of(
    errorHandler,
    logger, 
    queryHandler);
  
  return new MiddlewareQueryBus(middleware);
}
```

## Using the query bus in a controller

Let's suppose we have a query to search for upcoming music concerts.
Let's suppose we have a query handler class `SearchUpcomingConcertsAction` annotated with `@Service`.
And let's suppose that we have a command `SearchUpcomingConcertsInput`.

Below is the snippet that shows one way to implement this command in a RESTful API.

```java
@RestController
public final class SearchUpcomingConcertsController {
  private final ConcertResultsMapper mapper 
      = ConcertResultsMapper.INSTANCE;
  private final QueryBus bus;

  public SearchUpcomingConcertsController(QueryBus bus) {
    this.bus = bus;
  }

  @GetMapping("/v1/upcoming-concerts")
  public ResponseEntity<Object> enrollToPaperlessBilling(
    @Valid @RequestBody SearchUpcomingConcertsValues request
  ) {
    final SearchUpcomingConcertsInput input = request.input();
    final UpcomingConcertsResults results = bus.dispatch(input);
    return new ResponseEntity<>(
        mapper.map(results), HttpStatus.OK);
  }
}
```

In the example above `SearchUpcomingConcertsValues` would contain your regular validation annotations and `input` is a factory for your command.

Given the configuration of your query bus, explained in the previous section, the following would be true for this and all queries in your application (provided that you are using all the middleware provided byt this package).

1. Your query would be found and executed automatically
2. Its execution time would be logged, or,
3. In case of error it would log the exception information
