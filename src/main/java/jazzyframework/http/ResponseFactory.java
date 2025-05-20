/**
 * A utility class for creating response objects in a fluent way.
 * Inspired by Laravel's response() helper function.
 */
package jazzyframework.http;

import java.util.HashMap;
import java.util.Map;

public class ResponseFactory {
    
    /**
     * Returns a new instance of the ResponseFactory.
     * 
     * @return A new ResponseFactory
     */
    public static ResponseFactory response() {
        return new ResponseFactory();
    }
    
    /**
     * Creates a new JSON response.
     * 
     * @param data The data to include in the response
     * @return A Response object with the data as JSON
     */
    public Response json(Object data) {
        return Response.json(data);
    }
    
    /**
     * Creates a new JSON response with the provided key-value pairs.
     * 
     * @param keyValues The key-value pairs (must be even number of arguments)
     * @return A Response object with the key-value pairs as JSON
     * @throws IllegalArgumentException if an odd number of arguments is provided
     */
    public Response json(Object... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("Must provide an even number of arguments");
        }
        
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            if (!(keyValues[i] instanceof String)) {
                throw new IllegalArgumentException("Keys must be strings");
            }
            data.put((String) keyValues[i], keyValues[i + 1]);
        }
        
        return Response.json(data);
    }
    
    /**
     * Creates a new JSON response using the JSON builder.
     * 
     * @param json The JSON object built using the JSON class
     * @return A Response object with the JSON data
     */
    public Response json(JSON json) {
        return Response.json(json.toMap());
    }
    
    /**
     * Creates a success response with the specified message.
     * 
     * @param message The success message
     * @return A Response object with success status and message
     */
    public Response success(String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "success");
        data.put("message", message);
        return Response.json(data);
    }
    
    /**
     * Creates a success response with the specified message and data.
     * 
     * @param message The success message
     * @param data The data to include
     * @return A Response object with success status, message, and data
     */
    public Response success(String message, Object data) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", message);
        responseData.put("data", data);
        return Response.json(responseData);
    }
    
    /**
     * Creates a success response with the specified message and JSON data.
     * 
     * @param message The success message
     * @param json The JSON data to include
     * @return A Response object with success status, message, and data
     */
    public Response success(String message, JSON json) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", message);
        responseData.put("data", json.toMap());
        return Response.json(responseData);
    }
    
    /**
     * Creates an error response with the specified message.
     * 
     * @param message The error message
     * @return A Response object with error status and message
     */
    public Response error(String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "error");
        data.put("message", message);
        return Response.json(data).status(400);
    }
    
    /**
     * Creates an error response with the specified message and status code.
     * 
     * @param message The error message
     * @param statusCode The HTTP status code
     * @return A Response object with error status, message, and status code
     */
    public Response error(String message, int statusCode) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "error");
        data.put("message", message);
        return Response.json(data).status(statusCode);
    }
    
    /**
     * Creates a text response.
     * 
     * @param text The text content
     * @return A Response object with text content
     */
    public Response text(String text) {
        return Response.text(text);
    }
    
    /**
     * Creates an HTML response.
     * 
     * @param html The HTML content
     * @return A Response object with HTML content
     */
    public Response html(String html) {
        return Response.html(html);
    }
    
    /**
     * Creates a redirect response.
     * 
     * @param url The URL to redirect to
     * @return A Response object with redirect status and location
     */
    public Response redirect(String url) {
        return Response.redirect(url);
    }
} 