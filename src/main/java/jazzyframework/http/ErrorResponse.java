/**
 * Utility class for creating standardized error responses.
 * This provides a consistent format for all API error responses.
 */
package jazzyframework.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorResponse {
    
    /**
     * Creates a 400 Bad Request error response.
     * 
     * @param message The error message
     * @return A Response object with appropriate status and message
     */
    public static Response badRequest(String message) {
        return createError(400, "Bad Request", message);
    }
    
    /**
     * Creates a 401 Unauthorized error response.
     * 
     * @param message The error message
     * @return A Response object with appropriate status and message
     */
    public static Response unauthorized(String message) {
        return createError(401, "Unauthorized", message);
    }
    
    /**
     * Creates a 403 Forbidden error response.
     * 
     * @param message The error message
     * @return A Response object with appropriate status and message
     */
    public static Response forbidden(String message) {
        return createError(403, "Forbidden", message);
    }
    
    /**
     * Creates a 404 Not Found error response.
     * 
     * @param message The error message
     * @return A Response object with appropriate status and message
     */
    public static Response notFound(String message) {
        return createError(404, "Not Found", message);
    }
    
    /**
     * Creates a 405 Method Not Allowed error response.
     * 
     * @param message The error message
     * @return A Response object with appropriate status and message
     */
    public static Response methodNotAllowed(String message) {
        return createError(405, "Method Not Allowed", message);
    }
    
    /**
     * Creates a 422 Unprocessable Entity error response (validation errors).
     * 
     * @param message The error message
     * @return A Response object with appropriate status and message
     */
    public static Response validationError(String message) {
        return createError(422, "Validation Error", message);
    }
    
    /**
     * Creates a 429 Too Many Requests error response.
     * 
     * @param message The error message
     * @return A Response object with appropriate status and message
     */
    public static Response tooManyRequests(String message) {
        return createError(429, "Too Many Requests", message);
    }
    
    /**
     * Creates a 500 Internal Server Error response.
     * 
     * @param message The error message
     * @return A Response object with appropriate status and message
     */
    public static Response serverError(String message) {
        return createError(500, "Internal Server Error", message);
    }
    
    /**
     * Creates a custom error response with the specified status code and message.
     * 
     * @param statusCode The HTTP status code
     * @param error The error type
     * @param message The error message
     * @return A Response object with the specified status and message
     */
    public static Response createError(int statusCode, String error, String message) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("status", statusCode);
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        
        return Response.json(errorResponse).status(statusCode);
    }
} 