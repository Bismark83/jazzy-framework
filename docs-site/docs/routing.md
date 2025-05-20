# Routing

Jazzy's routing system provides an easy way to connect HTTP requests to appropriate controller methods. This document explains route definition, HTTP methods, and URL parameter usage.

## Basic Routing

In Jazzy, routing is managed by the `Router` class. Routes are created using HTTP method-specific definition methods.

### HTTP Method-Based Route Definition

```java
Router router = new Router();

// Define route for GET method
router.GET("/users/{id}", "getUserById", UserController.class);

// Define route for POST method
router.POST("/users", "createUser", UserController.class);

// Define route for PUT method
router.PUT("/users/{id}", "updateUser", UserController.class);

// Define route for DELETE method
router.DELETE("/users/{id}", "deleteUser", UserController.class);
```

## HTTP Methods

Jazzy supports the following HTTP methods:

- **GET**: Retrieve a resource
- **POST**: Create a new resource
- **PUT**: Update an existing resource
- **DELETE**: Delete a resource

## URL Parameters

In Jazzy, URL parameters are defined using the `{parameter_name}` format:

```java
router.GET("/users/{id}", "getUserById", UserController.class);
```

You can access these parameters from your controller method:

```java
public Response getUserById(Request request) {
    String id = request.path("id");
    // Process using id
    return response().json("id", id, "name", "John Doe");
}
```

## Query Parameters

Query parameters are taken directly from the URL and can be accessed from the Request object:

```java
public Response getAllUsers(Request request) {
    int page = request.queryInt("page", 1);  // Default value 1
    int limit = request.queryInt("limit", 10);  // Default value 10
    String sortBy = request.query("sort_by", "name");
    String sortOrder = request.query("sort_order", "asc");
    
    // Filter data using page and limit
    return response().json(
        "page", page,
        "limit", limit,
        "sort", JSON.of(
            "by", sortBy,
            "order", sortOrder
        ),
        "users", getUserList(page, limit)
    );
}
```

## Path Patterns

The Router considers path parameters when matching defined routes. For example:

- `/users` → Exact match
- `/users/{id}` → Parameterized match (e.g., `/users/123`)

If you define multiple routes with the same type and path, the first defined route is used.

## Controller Methods

In Jazzy, controller methods must have the following signature:

```java
public Response methodName(Request request) {
    // Operations
    return response;
}
```

Each controller method takes a `Request` object and returns a `Response` object.

## Example: Comprehensive Routing

Below is a comprehensive example showing different routes:

```java
// Config and Router setup
Config config = new Config();
config.setServerPort(8088);
config.setEnableMetrics(true); // "/metrics" endpoint is automatically added

Router router = new Router();

// User routes
router.GET("/users/{id}", "getUserById", UserController.class);
router.GET("/users", "getAllUsers", UserController.class);
router.POST("/users", "createUser", UserController.class);
router.PUT("/users/{id}", "updateUser", UserController.class);
router.DELETE("/users/{id}", "deleteUser", UserController.class);
router.POST("/users/with-rules", "createUserWithRules", UserController.class);

// Product routes
router.GET("/products/{id}", "getProduct", ProductController.class);
router.GET("/products", "listProducts", ProductController.class);
router.POST("/products", "createProduct", ProductController.class);
router.PUT("/products/{id}", "updateProduct", ProductController.class);
router.DELETE("/products/{id}", "deleteProduct", ProductController.class);
router.POST("/products/with-rules", "createProductWithRules", ProductController.class);

// Start the server
Server server = new Server(router, config);
server.start(config.getServerPort());
```

Controller examples:

```java
public class UserController {
    // Basic CRUD operations
    public Response getUserById(Request request) { 
        String id = request.path("id");
        return response().json("id", id, "name", "John Doe");
    }
    
    public Response getAllUsers(Request request) { 
        int page = request.queryInt("page", 1);
        return response().json("users", JSON.array(), "page", page);
    }
    
    public Response createUser(Request request) { 
        // Validation and registration operations
        return response().success("User created successfully");
    }
    
    public Response updateUser(Request request) { 
        String id = request.path("id");
        return response().success("User updated successfully");
    }
    
    public Response deleteUser(Request request) { 
        String id = request.path("id");
        return response().success("User deleted successfully");
    }
}
```

## Next Steps

- Read the request processing documentation to understand [HTTP Requests](requests.md)
- Look at the response documentation to create [HTTP Responses](responses.md)
- Check [Validation](validation.md) to validate request data 