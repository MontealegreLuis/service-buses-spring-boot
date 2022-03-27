# Query Bus

- [Configuration](#configuration)
- [Using the query bus in a controller](#using-the-query-bus-in-a-controller)

## Configuration

As commonly done in a Spring Boot application, we need to start by creating a `@Configuration` class.

```java
@Configuration
public class QueryBusConfiguration {}
```

In the following sections we'll explain how to add `@Bean`s to this configuration class to get your command bus up and running.

- [Query handler middleware](#query-handler-middleware)
- [Query logger middleware](#query-logger-middleware)
- [Query error handler middleware](#query-error-handler-middleware)
- [Query bus](#query-bus)

### Query handler middleware

We'll start by configuring the `QueryHandlerMiddleware`.
We'll use Spring Boot's [ApplicationContext](https://docs.spring.io/spring-framework/docs/2.5.x/reference/beans.html#context-introduction) as a factory for query handlers.

```java
@Bean
public QueryHandlerMiddleware queryHandlerMiddleware(
  ApplicationContext context) {
  var factory = new ApplicationContextQueryHandlerFactory(context);
  var locator = new ReflectionsQueryHandlerLocator("queries.package");
  return new QueryHandlerMiddleware(locator, factory);
}
```

As shown in the snippet above `ApplicationContext` can be passed directly to your `@Bean` factory method.

### Query logger middleware

To set up your [query logger middleware](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/query-bus/logging.md), you'll need the following factories.

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
public ActivityFeed activityFeed(Logger logger) {
  return new ActivityFeed(logger);
}

@Bean
public QueryLoggerMiddleware queryLoggerMiddleware(
  ActivityFeed feed, Clock clock) {
  return new QueryLoggerMiddleware(feed, clock);
}
```

### Query error handler middleware

To configure your [query error handler](https://github.com/MontealegreLuis/service-buses-middleware/blob/main/docs/query-bus/error-handler.md) you'll need the following beans.

```java
@Bean
public QueryErrorHandlerMiddleware queryErrorHandlerMiddleware(
    ActivityFeed feed, ObjectMapper mapper) {
  var serializer = new ContextSerializer(mapper);

  return new QueryErrorHandlerMiddleware(feed, serializer);
}
```

Unless you want more [specific configuration](https://github.com/MontealegreLuis/activity-feed#masking-sensitive-information) for your error handler, you can pass the `ObjectMapper` to your factory as shown in the snippet above.

### Query bus

You can choose from the sections above what middleware to configure and add to your query bus.
The snippet below shows how to create a bus with all the middleware provided in this package.

```java
@Bean
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
  private final QueryBus bus;

  public SearchUpcomingConcertsController(QueryBus bus) {
    this.bus = bus;
  }

  @GetMapping("/v1/upcoming-concerts")
  public ResponseEntity<Object> enrollToPaperlessBilling(
    @Valid @RequestBody SearchUpcomingConcertsValues request
  ) {
    SearchUpcomingConcertsInput input = request.input();
    UpcomingConcertsResults results = bus.dispatch(input);
    return new ResponseEntity<>(results, HttpStatus.OK);
  }
}
```

In the example above `SearchUpcomingConcertsValues` would contain your regular validation annotations and `input` is a factory for your command.

Given the configuration of your query bus, explained in the previous section, the following would be true for this and all queries in your application (provided that you are using all the middleware provided byt this package).

1. Your query would be found and executed automatically
2. Its execution time would be logged, or,
3. In case of error it would log the exception information
