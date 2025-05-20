# Jazzy Framework

Jazzy is a lightweight web framework for Java. It provides a minimal and easy-to-understand API for developing fast web applications.

## Features

- Simple and intuitive API
- Routing system with HTTP method support (GET, POST, PUT, DELETE, PATCH)
- URL path parameter support
- Automatic parameter mapping
- Simple metrics collection and reporting

## Quick Start

To develop a web application with Jazzy, you can follow this example code:

```java
// App.java
package com.example;

import jazzyframework.core.Config;
import jazzyframework.core.Server;
import jazzyframework.routing.Router;

public class App {
    public static void main(String[] args) {
        // Create configuration
        Config config = new Config();
        config.setEnableMetrics(true); // "/metrics" endpoint is automatically added
        config.setServerPort(8088);
        
        // Create router
        Router router = new Router();
        
        // Define routes
        router.GET("/user/{id}", "getUserById", UserController.class);
        router.POST("/user/{id}", "updateUser", UserController.class);
        router.GET("/users", "getAllUsers", UserController.class);
        router.DELETE("/user/{id}", "deleteUser", UserController.class);
        router.PUT("/user/{id}", "putUser", UserController.class);

        // Start the server
        Server server = new Server(router, config);
        server.start(config.getServerPort());
    }
}

// UserController.java
package com.example;

public class UserController {
    // Method with path parameter
    public String getUserById(String id) {
        return "User ID: " + id;
    }

    // Method without parameters
    public String getAllUsers() {
        return "All users";
    }

    // Method with path parameter and request body
    public String updateUser(String id, String requestBody) {
        return "Updated user " + id + " with data: " + requestBody;
    }
    
    // Method for handling DELETE requests
    public String deleteUser(String id) {
        return "Deleted user " + id;
    }
    
    // Method for handling PUT requests
    public String putUser(String id, String requestBody) {
        return "Updated user " + id + " with data: " + requestBody;
    }
}
```

For a more detailed example, check the `src/main/java/examples/basic` directory.

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
- `controllers/`: System controllers
  - `MetricsController.java`: Metrics reporting
- `examples/`: Example applications
  - `basic/`: A simple web API example

## Tests

Unit tests have been written to ensure the reliability of the framework. Test coverage includes:

- `RouterTest`: Tests for adding routes, finding routes, and path parameter operations
- `RouteTest`: Tests for the route data structure
- `MetricsTest`: Tests for metric counters and calculations
- `MetricsControllerTest`: Tests for the metrics controller

When adding new features or modifying existing code, it's important to update existing tests or add new tests to maintain the stability of the framework.

## Vision and Roadmap

Jazzy aims to simplify Java web development with a low learning curve, inspired by the elegance and developer-friendliness of Laravel in the PHP world. Our mission is to create a framework that enables developers to quickly build production-ready applications without the typical complexity associated with Java web frameworks.

We believe Java development doesn't have to be verbose or complicated. By bringing Laravel-like simplicity and expressiveness to Java, we want to make web development more accessible and enjoyable for Java developers.

Key principles:
- Simplicity over complexity
- Convention over configuration
- Rapid development without sacrificing performance
- Low learning curve for developers new to Java or web development

## Future Features

### Core Improvements
- JSON support with automatic serialization/deserialization
- Simple template engine
- Middleware system
- Form validation
- Database integration with simple ORM

### Developer Experience
- Centralized routing configuration (similar to Laravel's routes.php)
- Authentication system with easy JWT integration
- Simple project setup and scaffolding
- Comprehensive documentation with examples

## Contributing

**Jazzy is actively maintained and we welcome contributions of any size!**

We believe that open source thrives with community involvement, and we appreciate all types of contributions, whether you're fixing a typo, improving documentation, adding a new feature, or reporting a bug.

### Ways to Contribute

- **Code Contributions**: New features, bug fixes, performance improvements
- **Documentation**: Improving README, adding examples, writing tutorials
- **Testing**: Adding new tests, improving existing tests
- **Feedback**: Reporting bugs, suggesting features
- **Spreading the Word**: Telling others about Jazzy

### Getting Started

1. Fork the project
2. Clone your fork (`git clone https://github.com/yourusername/jazzy.git`)
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