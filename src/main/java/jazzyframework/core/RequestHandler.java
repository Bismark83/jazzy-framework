/**
 * Handles individual HTTP requests received by the server.
 * This class is responsible for parsing HTTP requests, routing them to the appropriate controller,
 * executing the controller method with the correct parameters, and returning the response to the client.
 * It also collects metrics for each request.
 */
package jazzyframework.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import jazzyframework.http.ErrorResponse;
import jazzyframework.http.Request;
import jazzyframework.http.Response;
import jazzyframework.http.validation.ValidationResult;
import jazzyframework.routing.Route;
import jazzyframework.routing.Router;

public class RequestHandler implements Runnable {
    public static final Logger logger = Logger.getLogger(RequestHandler.class.getName());
    private final Socket clientSocket;
    private final Router router;

    /**
     * Creates a new RequestHandler for the given client socket and router.
     * 
     * @param clientSocket The client socket to read from and write to
     * @param router The router to use for finding the appropriate controller
     */
    public RequestHandler(Socket clientSocket, Router router) {
        this.clientSocket = clientSocket;
        this.router = router;
    }

    /**
     * Handles the HTTP request.
     * This method parses the HTTP request, finds the appropriate controller,
     * executes the controller method with the correct parameters, and returns the response to the client.
     * It also collects metrics for each request.
     */
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            // Start time for metrics
            long startTime = System.currentTimeMillis();
            Metrics.totalRequests.incrementAndGet();

            // for example: GET /hello HTTP/1.1
            String line = in.readLine();
            if (line == null) {
                logger.warning("Client closed connection before sending request line.");
                return;
            }
            if (line.isEmpty()) {
                // Empty request line, ignore
                logger.fine("Empty request line received, ignoring.");
                return;
            }

            String[] parts = line.split(" ");
            if (parts.length < 2) {
                logger.warning("Malformed request line: " + line);
                out.write(ErrorResponse.badRequest("Malformed request line").toHttpResponse());
                out.flush();
                return;
            }
            String method = parts[0];
            String fullPath = parts[1];
            
            // Extract query parameters if present
            String path = fullPath;
            Map<String, String> queryParams = new HashMap<>();
            
            int queryIndex = fullPath.indexOf('?');
            if (queryIndex != -1) {
                path = fullPath.substring(0, queryIndex);
                String queryString = fullPath.substring(queryIndex + 1);
                queryParams = parseQueryString(queryString);
            }

            logger.info("Received request: " + method + " " + path);

            // Validate HTTP method supported by your router
            if (!router.isSupportedMethod(method)) {
                logger.warning("Unsupported HTTP method: " + method);
                out.write(ErrorResponse.methodNotAllowed("Method Not Allowed")
                    .header("Allow", "GET, POST, PUT, DELETE, PATCH")
                    .toHttpResponse());
                out.flush();
                return;
            }

            Map<String, String> headers = new HashMap<>();
            
            // Read headers
            String headerLine;
            int contentLength = 0;
            String contentType = null;
            while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                String[] headerParts = headerLine.split(":", 2);
                if (headerParts.length == 2) {
                    String headerName = headerParts[0].trim().toLowerCase();
                    String headerValue = headerParts[1].trim();
                    headers.put(headerName, headerValue);
                    
                    if (headerName.equals("content-length")) {
                        try {
                            contentLength = Integer.parseInt(headerValue);
                        } catch (NumberFormatException e) {
                            logger.warning("Invalid Content-Length header");
                            out.write(ErrorResponse.badRequest("Invalid Content-Length").toHttpResponse());
                            out.flush();
                            return;
                        }
                    }
                    if (headerName.equals("content-type")) {
                        contentType = headerValue;
                    }
                }
            }

            // Validate body presence only for allowed methods
            if (contentLength > 0 && !(method.equals("POST") || method.equals("PUT") || method.equals("PATCH"))) {
                logger.warning("Request with body on method that shouldn't have one: " + method);
                out.write(ErrorResponse.badRequest("Body not allowed for method " + method).toHttpResponse());
                out.flush();
                return;
            }

            // Read body if content length is present
            String body = "";
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                int read = in.read(buffer, 0, contentLength);
                if (read > 0) {
                    body = new String(buffer, 0, read);
                    logger.fine("Request body read with length: " + body.length());
                }
            }
            
            // Validate route existence
            Route route = router.findRoute(method, path);
            if (route == null) {
                logger.warning("Route not found: " + method + " " + path);
                out.write(ErrorResponse.notFound("Route not found: " + method + " " + path).toHttpResponse());
                out.flush();
                return;
            }

            logger.info("Found route: " + method + " " + route.getPath() + " -> " + route.getControllerClass().getSimpleName() + "." + route.getMethodName());
            
            Map<String, String> pathParams = router.extractPathParams(route.getPath(), path);
            logger.info("Extracted path params: " + pathParams);
            logger.info("Query params: " + queryParams);
            
            Class<?> controllerClass = route.getControllerClass();
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            
            // Find the controller method
            Method controllerMethod = findControllerMethod(controllerClass, route.getMethodName());
            if (controllerMethod == null) {
                logger.warning("Method not found: " + route.getMethodName());
                out.write(ErrorResponse.serverError("Method not found: " + route.getMethodName()).toHttpResponse());
                out.flush();
                return;
            }
            
            // Create Request object
            Request request = new Request(method, path, headers, pathParams, queryParams, body);
            
            // Check if request requires a body but body is empty
            boolean isBodyRequired = method.equals("POST") || method.equals("PUT") || method.equals("PATCH");
            boolean isBodyEmpty = body == null || body.trim().isEmpty();
            
            if (isBodyRequired && isBodyEmpty) {
                logger.warning("Empty request body for " + method + " request to " + path);
                out.write(ErrorResponse.badRequest("Request body is required for " + method + " requests").toHttpResponse());
                out.flush();
                return;
            }
            
            try {
                // Call the method with the Request object
                Object result = controllerMethod.invoke(controllerInstance, request);
                
                // Handle different return types
                if (result instanceof Response) {
                    // If the controller returns a Response object, use it directly
                    out.write(((Response) result).toHttpResponse());
                } else if (result instanceof String) {
                    // If the controller returns a String, wrap it in a text response
                    out.write(Response.text((String) result).toHttpResponse());
                } else if (result instanceof ValidationResult) {
                    // If the controller returns a ValidationResult, create a validation error response
                    ValidationResult validationResult = (ValidationResult) result;
                    if (!validationResult.isValid()) {
                        out.write(ErrorResponse.validationError("Validation failed: " + validationResult.getFirstErrors())
                                .toHttpResponse());
                    } else {
                        out.write(Response.json(Collections.singletonMap("message", "Validation succeeded"))
                                .toHttpResponse());
                    }
                } else {
                    // For any other object, convert it to JSON
                    out.write(Response.json(result).toHttpResponse());
                }
            } catch (IllegalArgumentException e) {
                // This will catch validation errors like missing required parameters
                logger.warning("Bad request: " + e.getMessage());
                out.write(ErrorResponse.badRequest(e.getMessage()).toHttpResponse());
            }
            
            out.flush();
            Metrics.successfulRequests.incrementAndGet();
            logger.info("Response sent successfully.");
            long duration = System.currentTimeMillis() - startTime;
            Metrics.totalResponseTime.addAndGet(duration);
        } catch (Exception e) {
            Metrics.failedRequests.incrementAndGet();
            logger.severe("Exception handling request: " + e.getMessage());
            e.printStackTrace();
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                out.write(ErrorResponse.serverError(e.getMessage()).toHttpResponse());
                out.flush();
            } catch (Exception ex) {
                logger.severe("Error sending error response: " + ex.getMessage());
            }
        } finally {
            try {
                clientSocket.close();
                logger.fine("Client socket closed.");
            } catch (Exception e) {
                logger.warning("Error closing client socket: " + e.getMessage());
            }
        }
    }
    
    /**
     * Parses a query string into a map of parameter names to values.
     * 
     * @param queryString The query string to parse
     * @return A map of parameter names to values
     */
    private Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryParams = new HashMap<>();
        String[] pairs = queryString.split("&");
        
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                queryParams.put(key, value);
            }
        }
        
        return queryParams;
    }
    
    /**
     * Find a controller method by name.
     * 
     * @param controllerClass The controller class
     * @param methodName The method name
     * @return The method if found, null otherwise
     */
    private Method findControllerMethod(Class<?> controllerClass, String methodName) {
        for (Method method : controllerClass.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
