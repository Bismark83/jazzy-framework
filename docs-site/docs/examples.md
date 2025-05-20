# Examples

This page provides comprehensive examples of using Jazzy Framework in different scenarios.

## Basic REST API

Here's a complete example of a basic REST API for a user management system:

```java
import static jazzyframework.http.ResponseFactory.response;
import jazzyframework.http.Request;
import jazzyframework.http.Response;
import jazzyframework.http.validation.ValidationResult;
import jazzyframework.routing.Router;

public class UserApi {

    public static void main(String[] args) {
        // Create a new router
        Router router = new Router();
        
        // Define routes
        router.get("/api/users", UserApi::getAllUsers);
        router.get("/api/users/{id}", UserApi::getUserById);
        router.post("/api/users", UserApi::createUser);
        router.put("/api/users/{id}", UserApi::updateUser);
        router.delete("/api/users/{id}", UserApi::deleteUser);
        
        // Start the server
        router.startServer(8080);
    }
    
    // Get all users
    public static Response getAllUsers(Request request) {
        // Get query parameters for pagination
        int page = request.queryInt("page", 1);
        int limit = request.queryInt("limit", 10);
        
        // Fetch users from database (example)
        List<User> users = UserService.getUsers(page, limit);
        int total = UserService.countUsers();
        
        return response().json(
            "users", users,
            "page", page,
            "limit", limit,
            "total", total
        );
    }
    
    // Get user by ID
    public static Response getUserById(Request request) {
        String id = request.path("id");
        
        // Find user
        User user = UserService.findById(id);
        
        if (user == null) {
            return response().json(
                "status", "error",
                "message", "User not found"
            ).status(404);
        }
        
        return response().json(user);
    }
    
    // Create new user
    public static Response createUser(Request request) {
        // Validate input
        ValidationResult result = request.validator()
            .field("name").required().minLength(3)
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
        
        // Parse JSON data
        Map<String, Object> userData = request.parseJson();
        
        // Create user (example)
        User newUser = UserService.createUser(
            (String) userData.get("name"),
            (String) userData.get("email"),
            (String) userData.get("password")
        );
        
        // Return response with created user
        return response().json(
            "status", "success",
            "message", "User created successfully",
            "user", newUser
        ).status(201);
    }
    
    // Update user
    public static Response updateUser(Request request) {
        String id = request.path("id");
        
        // Check if user exists
        if (!UserService.exists(id)) {
            return response().json(
                "status", "error",
                "message", "User not found"
            ).status(404);
        }
        
        // Validate input
        ValidationResult result = request.validator()
            .field("name").optional().minLength(3)
            .field("email").optional().email()
            .validate();
        
        if (!result.isValid()) {
            return response().json(
                "status", "error",
                "message", "Validation failed",
                "errors", result.getAllErrors()
            ).status(400);
        }
        
        // Parse JSON data
        Map<String, Object> userData = request.parseJson();
        
        // Update user (example)
        User updatedUser = UserService.updateUser(id, userData);
        
        return response().success("User updated successfully", updatedUser);
    }
    
    // Delete user
    public static Response deleteUser(Request request) {
        String id = request.path("id");
        
        // Check if user exists
        if (!UserService.exists(id)) {
            return response().json(
                "status", "error",
                "message", "User not found"
            ).status(404);
        }
        
        // Delete user
        UserService.deleteUser(id);
        
        return response().success("User deleted successfully");
    }
}
```

## Data Validation Example

Here's an example of using complex validation with custom validation rules:

```java
import jazzyframework.http.validation.ValidationRules;

// Create a validation rules class
public class ProductCreateRules extends ValidationRules {
    
    public ProductCreateRules() {
        field("name").required().minLength(3).maxLength(100);
        field("price").required().min(0.01);
        field("description").optional().maxLength(1000);
        field("category").required().in("electronics", "clothing", "food", "books");
        field("tags").optional();
        field("stock").required().min(0);
        field("sku").required().pattern("^[A-Z]{2}-\\d{4}-[A-Z]{2}$", 
            "SKU must be in format XX-0000-XX");
    }
}

// Use in controller
public Response createProduct(Request request) {
    ValidationResult result = request.validate(new ProductCreateRules());
    
    if (!result.isValid()) {
        return response().json(
            "status", "error",
            "message", "Validation failed",
            "errors", result.getAllErrors()
        ).status(400);
    }
    
    // Convert JSON data to Product object
    Product product = request.toObject(Product.class);
    
    // Save product
    productRepository.save(product);
    
    // Return success response
    return response().success("Product created successfully", product)
        .status(201);
}
```

## File Upload API

Here's how to handle file uploads in Jazzy:

```java
public Response uploadImage(Request request) {
    // Get file from request
    UploadedFile file = request.file("image");
    
    if (file == null) {
        return response().json(
            "status", "error",
            "message", "No image file uploaded"
        ).status(400);
    }
    
    // Validate file type
    if (!file.getContentType().startsWith("image/")) {
        return response().json(
            "status", "error",
            "message", "Uploaded file is not an image"
        ).status(400);
    }
    
    // Save file to disk
    String filename = System.currentTimeMillis() + "_" + file.getFilename();
    String storagePath = "uploads/" + filename;
    
    try {
        file.saveAs(storagePath);
    } catch (IOException e) {
        return response().json(
            "status", "error",
            "message", "Failed to save uploaded file"
        ).status(500);
    }
    
    // Return success response
    return response().json(
        "status", "success",
        "message", "File uploaded successfully",
        "filename", filename,
        "path", storagePath,
        "size", file.getSize(),
        "contentType", file.getContentType()
    );
}
```

## Authentication Middleware Example

Here's how to implement a simple authentication middleware:

```java
import jazzyframework.http.middleware.Middleware;

public class AuthMiddleware implements Middleware {
    
    @Override
    public Response handle(Request request, MiddlewareChain chain) {
        // Get authorization header
        String token = request.header("Authorization");
        
        if (token == null || !token.startsWith("Bearer ")) {
            return response().json(
                "status", "error",
                "message", "Unauthorized"
            ).status(401);
        }
        
        // Extract token value
        token = token.substring(7);
        
        try {
            // Verify token and get user (example)
            User user = TokenService.verifyToken(token);
            
            // Add user to request for later use in controllers
            request.setAttribute("user", user);
            
            // Continue to next middleware or controller
            return chain.next(request);
            
        } catch (Exception e) {
            return response().json(
                "status", "error",
                "message", "Invalid or expired token"
            ).status(401);
        }
    }
}

// Apply middleware to specific routes
router.get("/api/profile", profileController::getProfile).with(new AuthMiddleware());
router.group("/api/admin", adminRoutes -> {
    adminRoutes.get("/users", adminController::getUsers);
    adminRoutes.post("/settings", adminController::updateSettings);
}).with(new AuthMiddleware());
```

## Next Steps

For more information on specific components:

- [Getting Started](getting-started.md)
- [Routing](routing.md)
- [Requests](requests.md)
- [Responses](responses.md)
- [Validation](validation.md)
- [JSON Operations](json.md) 