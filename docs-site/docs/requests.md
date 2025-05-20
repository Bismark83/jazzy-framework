# HTTP Requests

In Jazzy, HTTP requests are handled using the `Request` class. This class allows you to easily access request components such as URL parameters, query parameters, headers, and request body.

## Request Object

Each controller method receives a `Request` object. This object contains all information about the incoming HTTP request.

```java
public Response getUserById(Request request) {
    // Use the Request object to process
}
```

## URL Parameters (Path Parameters)

You can access URL parameters using the `path()` method:

```java
public Response getUserById(Request request) {
    String id = request.path("id");
    
    // Process using the id parameter
    return response().json("id", id);
}
```

This retrieves the value of the `{id}` parameter in a path pattern like `/users/{id}`.

## Query Parameters

You can access query parameters using the `query()` method:

```java
public Response searchUsers(Request request) {
    String name = request.query("name");
    String email = request.query("email");
    
    // Query parameters with default values
    int page = request.queryInt("page", 1);
    int limit = request.queryInt("limit", 10);
    boolean active = request.queryBoolean("active", true);
    
    return response().json(
        "name", name,
        "email", email,
        "page", page,
        "limit", limit,
        "active", active
    );
}
```

## Headers

You can access headers using the `header()` method:

```java
public Response exampleWithHeaders(Request request) {
    // Get headers
    String contentType = request.header("Content-Type");
    String authorization = request.header("Authorization");
    
    // Header with default value
    String userAgent = request.header("User-Agent", "Unknown");
    
    return response().json(
        "contentType", contentType,
        "authorization", authorization,
        "userAgent", userAgent
    );
}
```

## Request Body

### Processing JSON Body

The `parseJson()` method is used to process the JSON request body:

```java
public Response createUser(Request request) {
    // Get JSON body as a Map
    Map<String, Object> userData = request.parseJson();
    
    String name = (String) userData.get("name");
    String email = (String) userData.get("email");
    int age = ((Number) userData.get("age")).intValue();
    
    // Process using the data
    return response().success("User created successfully");
}
```

### Converting to Java Object

The `toObject()` method is used to directly convert the JSON request body to a Java object:

```java
public Response createUser(Request request) {
    // Convert JSON body to User object
    User user = request.toObject(User.class);
    
    // Process using the User object
    user.setId(generateId());
    saveUser(user);
    
    return response().success("User created successfully", user);
}
```

For this conversion to work, your object must comply with Java bean standards (getter/setter methods or public fields).

## Data Validation

You can use the `validator()` method to validate request data:

```java
public Response createUser(Request request) {
    // Validation process
    ValidationResult result = request.validator()
        .field("name").required().minLength(3).maxLength(50)
        .field("email").required().email()
        .field("password").required().minLength(8)
            .pattern("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$", 
                "Password must contain at least one uppercase letter, one lowercase letter, and one number")
        .field("role").in("admin", "user", "editor")
        .validate();
    
    if (!result.isValid()) {
        return response().json(
            "status", "error",
            "message", "Validation failed",
            "errors", result.getAllErrors()
        ).status(400);
    }
    
    // Validation successful, continue processing
    Map<String, Object> userData = request.parseJson();
    
    // Perform operations
    String newId = "user-" + System.currentTimeMillis();
    userData.put("id", newId);
    
    return response().success("User created successfully", userData).status(201);
}
```

You can use `ValidationRules` classes for more comprehensive validation:

```java
public Response createUserWithRules(Request request) {
    // Validation using UserCreateRules class
    ValidationResult result = request.validate(new UserCreateRules());
    
    if (!result.isValid()) {
        return response().json(
            "status", "error",
            "message", "Validation failed",
            "errors", result.getAllErrors()
        ).status(400);
    }
    
    // Validation successful, continue processing
    Map<String, Object> userData = request.parseJson();
    userData.remove("password");
    String newId = "user-" + System.currentTimeMillis();
    userData.put("id", newId);
    
    return response().success("User created successfully", userData).status(201);
}
```

You can define the ValidationRules class as follows:

```java
public class UserCreateRules extends ValidationRules {
    
    public UserCreateRules() {
        field("name").required().minLength(3).maxLength(50);
        field("email").required().email();
        field("password").required().minLength(8)
            .pattern("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$", 
                "Password must contain at least one uppercase letter, one lowercase letter, and one number");
        field("role").in("admin", "user", "editor");
    }
}
```

See the [Validation](validation.md) document for detailed validation.

## Request Information

You can also get other information from the Request object:

```java
public Response requestInfo(Request request) {
    String method = request.getMethod();  // HTTP method (GET, POST, etc.)
    String path = request.getPath();      // Request path
    String body = request.getBody();      // Raw request body
    
    Map<String, String> headers = request.getHeaders();        // All headers
    Map<String, String> pathParams = request.getPathParams();  // All path parameters
    Map<String, String> queryParams = request.getQueryParams(); // All query parameters
    
    return response().json(
        "method", method,
        "path", path,
        "bodyLength", body.length(),
        "headerCount", headers.size(),
        "pathParamCount", pathParams.size(),
        "queryParamCount", queryParams.size()
    );
}
```

## Example: Comprehensive Request Processing

```java
public Response processOrder(Request request) {
    // Get path parameter
    String orderId = request.path("id");
    
    // Get query parameters
    boolean includeDetails = request.queryBoolean("details", false);
    
    // Check header
    String authorization = request.header("Authorization");
    if (authorization == null || !isValidToken(authorization)) {
        return response().json(
            "status", "error",
            "message", "Unauthorized"
        ).status(401);
    }
    
    // Validate body
    ValidationResult result = request.validator()
        .field("items").required()
        .field("customer").required()
        .validate();
    
    if (!result.isValid()) {
        return response().json(
            "status", "error",
            "message", "Validation failed",
            "errors", result.getAllErrors()
        ).status(400);
    }
    
    // Validation successful, convert to object
    Map<String, Object> orderData = request.parseJson();
    orderData.put("id", orderId);
    
    // Process
    // ...
    
    // Return response
    return response().success("Order processed successfully", orderData);
}
```

## Next Steps

- Learn how to create [HTTP Responses](responses.md)
- Explore [Validation](validation.md) mechanisms in more detail
- Learn about [JSON Operations](json.md) 