# Jazzy Framework

Jazzy is a lightweight web framework for Java. It provides a minimal and easy-to-understand API for developing fast web applications with a structure inspired by Laravel.

## Features

- Simple and intuitive API
- Routing system with HTTP method support (GET, POST, PUT, DELETE, PATCH)
- URL path parameter support
- Request validation with comprehensive rules
- JSON response generation with fluent API
- Metrics collection and reporting

## Quick Start

To develop a web application with Jazzy, you can follow this example code:

```java
// App.java
package examples.basic;

import jazzyframework.core.Config;
import jazzyframework.core.Server;
import jazzyframework.routing.Router;

public class App 
{
    public static void main( String[] args )
    {
        Config config = new Config();
        config.setEnableMetrics(true); // "/metrics" endpoint is automatically added
        config.setServerPort(8088);

        Router router = new Router();
        
        // User routes
        router.GET("/users/{id}", "getUserById", UserController.class);
        router.GET("/users", "getAllUsers", UserController.class);
        router.POST("/users", "createUser", UserController.class);
        router.PUT("/users/{id}", "updateUser", UserController.class);
        router.DELETE("/users/{id}", "deleteUser", UserController.class);
        
        // Start the server
        Server server = new Server(router, config);
        server.start(config.getServerPort());
    }
}

// UserController.java
package examples.basic;

import static jazzyframework.http.ResponseFactory.response;
import jazzyframework.http.Request;
import jazzyframework.http.Response;
import jazzyframework.http.validation.ValidationResult;
import java.util.Map;

public class UserController {
    
    public Response getUserById(Request request) {
        String id = request.path("id");
        
        return response().json(
            "id", id,
            "name", "Fletcher Davidson",
            "email", "fletcher@example.com"
        );
    }

    public Response getAllUsers(Request request) {
        int page = request.queryInt("page", 1);
        int limit = request.queryInt("limit", 10);
        
        return response().json(
            "users", JSON.array(
                JSON.of("id", "user-1", "name", "Fletcher Davidson"),
                JSON.of("id", "user-2", "name", "Jane Smith")
            ),
            "page", page,
            "limit", limit,
            "total", 2
        );
    }

    public Response createUser(Request request) {
        // Validate the request
        ValidationResult result = request.validator()
            .field("name").required().minLength(3).maxLength(50)
            .field("email").required().email()
            .field("password").required().minLength(8)
            .validate();
        
        if (!result.isValid()) {
            return response().json(
                "status", "error",
                "message", "Validation failed",
                "errors", result.getAllErrors()
            ).status(400);
        }
        
        Map<String, Object> userData = request.parseJson();
        
        // Generate a new ID
        String newId = "user-" + System.currentTimeMillis();
        userData.put("id", newId);
        
        return response().success("User created successfully", userData).status(201);
    }
    
    // Additional methods for update and delete operations
}
```

For a more detailed example, check the `src/main/java/examples/basic` directory.

## Documentation

Complete documentation for Jazzy Framework is available on our GitHub Pages site:

[Jazzy Framework Documentation](https://canermastan.github.io/jazzy-framework/)

## Development

Jazzy is developed with Maven. After cloning the project, you can use the following commands:

```bash
# Install dependencies and build the project
mvn clean install

# Run tests
mvn test

# Run the example application
mvn exec:java -Dexec.mainClass="examples.basic.App"
```

## Project Structure

- `core/`: Core framework components
  - `Server.java`: HTTP server
  - `RequestHandler.java`: HTTP request processor
  - `Config.java`: Configuration management
  - `Metrics.java`: Performance metrics
- `routing/`: Routing system
  - `Router.java`: Route management
  - `Route.java`: Route data structure
- `http/`: HTTP handling
  - `Request.java`: Request handling
  - `Response.java`: Response building
  - `ResponseFactory.java`: Factory for creating responses
  - `JSON.java`: JSON creation utilities
  - `validation/`: Validation system
- `controllers/`: System controllers
  - `MetricsController.java`: Metrics reporting
- `examples/`: Example applications
  - `basic/`: A simple web API example

## Tests

Unit tests have been written to ensure the reliability of the framework. Test coverage includes:

- `RouterTest`: Tests for adding routes, finding routes, and path parameter operations
- `RouteTest`: Tests for the route data structure
- `MetricsTest`: Tests for metric counters and calculations
- `ValidationTest`: Tests for the validation system
- `ResponseFactoryTest`: Tests for response generation

When adding new features or modifying existing code, it's important to update existing tests or add new tests to maintain the stability of the framework.

## Roadmap

Jazzy is actively being developed with the following features planned for upcoming releases:

### Upcoming Features

- **Middleware System**: Support for request/response middleware chains
- **Database Integration**: jOOQ integration for type-safe SQL queries
- **Dependency Injection**: Custom DI container (PococContainer) with `@Named` and `@Qualified` annotations
- **Security Framework**: Authentication and authorization system
- **Caching System**: Redis integration for high-performance caching
- **API Documentation**: Swagger/OpenAPI integration
- **WebSocket Support**: Real-time bidirectional communication
- **Task Scheduling**: Cron-style job scheduling
- **Monitoring Tools**: Health checks and system monitoring
- **File Storage**: Cloud storage integrations (S3, etc)
- **Event System**: Pub/sub event handling
- **CLI Tools**: Command line tools for code generation
- **And More...**: Stay tuned for additional features!


## Contributing

**Jazzy is actively maintained and we welcome contributions of any size!**

We believe that open source thrives with community involvement, and we appreciate all types of contributions, whether you're fixing a typo, improving documentation, adding a new feature, or reporting a bug.

### Getting Started

1. Fork the project
2. Clone your fork (`git clone https://github.com/canermastan/jazzy-framework.git`)
3. Create a feature branch (`git checkout -b feature/amazing-feature`)
4. Make your changes (don't forget to add tests if applicable)
5. Run tests to make sure everything works (`mvn test`)
6. Commit your changes (`git commit -m 'Add some amazing feature'`)
7. Push to the branch (`git push origin feature/amazing-feature`)
8. Open a Pull Request

### Pull Request Guidelines

- Keep your changes focused on a single issue
- Make sure all tests pass
- Update documentation if needed
- Follow existing code style

No contribution is too small, and we're happy to help newcomers get started!

## License

MIT License 