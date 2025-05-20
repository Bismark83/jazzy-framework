/**
 * Example controller for user operations.
 * Demonstrates how to handle different HTTP methods and path parameters using the Request class.
 */
package examples.basic;

import static jazzyframework.http.ResponseFactory.response;

import java.util.Map;
import java.util.logging.Logger;

import jazzyframework.http.JSON;
import jazzyframework.http.Request;
import jazzyframework.http.Response;
import jazzyframework.http.validation.ValidationResult;
import examples.basic.validation.UserCreateRules;

public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());
    
    /**
     * Gets a user by ID.
     * 
     * @param request The HTTP request
     * @return A Response object with user data
     */
    public Response getUserById(Request request) {
        String id = request.path("id");
        
        return response().json(
            "id", id,
            "name", "John Doe",
            "email", "john@example.com",
            "role", "user",
            "created_at", "2023-01-15"
        );
    }

    /**
     * Gets all users with pagination and filtering.
     * 
     * @param request The HTTP request
     * @return A Response object with a list of users
     */
    public Response getAllUsers(Request request) {
        int page = request.queryInt("page", 1);
        int limit = request.queryInt("limit", 10);
        String sortBy = request.query("sort_by", "name");
        String sortOrder = request.query("sort_order", "asc");
        
        logger.info("Fetching users with page: " + page + ", limit: " + limit);
        logger.info("Sorting by: " + sortBy + " " + sortOrder);
        
        return response().json(
            "users", JSON.array(
                JSON.of(
                    "id", "user-1",
                    "name", "John Doe",
                    "email", "john@example.com",
                    "role", "admin"
                ),
                JSON.of(
                    "id", "user-2",
                    "name", "Jane Smith",
                    "email", "jane@example.com",
                    "role", "user"
                ),
                JSON.of(
                    "id", "user-3",
                    "name", "Mike Johnson",
                    "email", "mike@example.com",
                    "role", "user"
                )
            ),
            "total", 3,
            "page", page,
            "limit", limit,
            "sort", JSON.of(
                "by", sortBy,
                "order", sortOrder
            )
        );
    }

    /**
     * Creates a new user with validation.
     * 
     * @param request The HTTP request
     * @return A Response object with the created user
     */
    public Response createUser(Request request) {
        // Validate the request
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
        
        // Extract fields from the validated request
        Map<String, Object> userData = request.parseJson();
        
        // Hide password in the response
        userData.remove("password");
        
        // Generate a new ID (would be handled by database in real app)
        String newId = "user-" + System.currentTimeMillis();
        userData.put("id", newId);
        userData.put("created_at", "2023-05-20");
        
        return response().success("User created successfully", userData).status(201);
    }
    
    /**
     * Updates a user with validation.
     * 
     * @param request The HTTP request
     * @return A Response object confirming the update
     */
    public Response updateUser(Request request) {
        String id = request.path("id");
        
        // Validate the request - note that fields aren't required for updates
        ValidationResult result = request.validator()
            .field("name").minLength(3).maxLength(50)
            .field("email").email()
            .field("password").minLength(8)
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
        
        // Extract fields from the validated request
        Map<String, Object> userData = request.parseJson();
        
        // Hide password in the response
        userData.remove("password");
        userData.put("id", id);
        userData.put("updated_at", "2023-05-20");
        
        return response().success("User updated successfully", userData);
    }
    
    /**
     * Deletes a user.
     * 
     * @param request The HTTP request
     * @return A Response object confirming the deletion
     */
    public Response deleteUser(Request request) {
        String id = request.path("id");
        
        return response().success("User deleted successfully", 
            JSON.of("id", id)
        );
    }
    
    /**
     * Creates a user with UserCreateRules.
     * Shows how to use a separate validation rules class.
     * 
     * @param request The HTTP request
     * @return A Response with the created user
     */
    public Response createUserWithRules(Request request) {
        ValidationResult result = request.validate(new UserCreateRules());
        
        if (!result.isValid()) {
            return response().json(
                "status", "error",
                "message", "Validation failed",
                "errors", result.getAllErrors()
            ).status(400);
        }
        
        Map<String, Object> userData = request.parseJson();
        userData.remove("password");
        String newId = "user-" + System.currentTimeMillis();
        userData.put("id", newId);
        userData.put("created_at", "2023-05-20");
        
        return response().success("User created successfully", userData).status(201);
    }
} 