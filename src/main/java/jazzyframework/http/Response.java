/**
 * Response class to handle HTTP responses in a fluent way.
 * Inspired by Laravel's response handling approach.
 */
package jazzyframework.http;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Response {
    private int statusCode;
    private String body;
    private String contentType;
    private Map<String, String> headers;

    /**
     * Creates a new Response with default values.
     */
    private Response() {
        this.statusCode = 200;
        this.body = "";
        this.contentType = "text/plain";
        this.headers = new HashMap<>();
    }

    /**
     * Creates a JSON response.
     * 
     * @param body The JSON content or object to be converted to JSON
     * @return A new Response instance
     */
    public static Response json(Object body) {
        Response response = new Response();
        response.contentType = "application/json";
        response.body = body instanceof String ? (String) body : convertToJson(body);
        return response;
    }

    /**
     * Creates a plain text response.
     * 
     * @param body The text content
     * @return A new Response instance
     */
    public static Response text(String body) {
        Response response = new Response();
        response.contentType = "text/plain";
        response.body = body;
        return response;
    }

    /**
     * Creates an HTML response.
     * 
     * @param html The HTML content
     * @return A new Response instance
     */
    public static Response html(String html) {
        Response response = new Response();
        response.contentType = "text/html";
        response.body = html;
        return response;
    }

    /**
     * Creates a redirect response.
     * 
     * @param url The URL to redirect to
     * @return A new Response instance
     */
    public static Response redirect(String url) {
        Response response = new Response();
        response.statusCode = 302;
        response.headers.put("Location", url);
        return response;
    }

    /**
     * Sets the HTTP status code.
     * 
     * @param statusCode The HTTP status code
     * @return This Response instance for method chaining
     */
    public Response status(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * Adds a header to the response.
     * 
     * @param name The header name
     * @param value The header value
     * @return This Response instance for method chaining
     */
    public Response header(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * Sets the content type of the response.
     * 
     * @param contentType The content type
     * @return This Response instance for method chaining
     */
    public Response contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Gets the HTTP status code.
     * 
     * @return The HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets the response body.
     * 
     * @return The response body
     */
    public String getBody() {
        return body;
    }

    /**
     * Gets the content type.
     * 
     * @return The content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Gets all headers.
     * 
     * @return The headers as a Map
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Converts the response to an HTTP format.
     * 
     * @return The HTTP response string
     */
    public String toHttpResponse() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(getStatusMessage(statusCode)).append("\r\n");
        sb.append("Content-Type: ").append(contentType).append("\r\n");
        sb.append("Content-Length: ").append(body.getBytes().length).append("\r\n");
        
        for (Map.Entry<String, String> header : headers.entrySet()) {
            sb.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        
        sb.append("\r\n");
        sb.append(body);
        
        return sb.toString();
    }
    
    /**
     * Utility method to convert an object to JSON.
     * Note: This is a simple implementation. In a real application, use a proper JSON library.
     * 
     * @param obj The object to convert
     * @return The JSON string
     */
    private static String convertToJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            StringBuilder json = new StringBuilder("{");
            boolean first = true;
            
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) {
                    json.append(",");
                }
                first = false;
                json.append("\"").append(entry.getKey()).append("\":");
                json.append(convertToJson(entry.getValue()));
            }
            
            json.append("}");
            return json.toString();
        }
        
        if (obj.getClass().isArray()) {
            StringBuilder json = new StringBuilder("[");
            int length = Array.getLength(obj);
            
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    json.append(",");
                }
                json.append(convertToJson(Array.get(obj, i)));
            }
            
            json.append("]");
            return json.toString();
        }
        
        if (obj instanceof Iterable) {
            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            
            for (Object item : (Iterable<?>) obj) {
                if (!first) {
                    json.append(",");
                }
                first = false;
                json.append(convertToJson(item));
            }
            
            json.append("]");
            return json.toString();
        }
        
        if (obj instanceof String) {
            return "\"" + ((String) obj).replace("\"", "\\\"") + "\"";
        }
        
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        
        // Handle custom objects using reflection
        if (!obj.getClass().getName().startsWith("java.")) {
            try {
                StringBuilder json = new StringBuilder("{");
                boolean first = true;
                
                Field[] fields = obj.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    
                    if (!first) {
                        json.append(",");
                    }
                    first = false;
                    
                    json.append("\"").append(field.getName()).append("\":");
                    json.append(convertToJson(field.get(obj)));
                }
                
                json.append("}");
                return json.toString();
            } catch (Exception e) {
                // Fall back to toString if reflection fails
            }
        }
        
        // Basic fallback for other objects
        return "\"" + obj.toString() + "\"";
    }
    
    /**
     * Gets the status message for a status code.
     * 
     * @param statusCode The HTTP status code
     * @return The status message
     */
    private String getStatusMessage(int statusCode) {
        switch (statusCode) {
            case 200: return "OK";
            case 201: return "Created";
            case 204: return "No Content";
            case 301: return "Moved Permanently";
            case 302: return "Found";
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 405: return "Method Not Allowed";
            case 500: return "Internal Server Error";
            default: return "Unknown";
        }
    }
} 