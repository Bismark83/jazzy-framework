/**
 * Represents a route in the Jazzy Framework.
 * A route maps an HTTP method and URL path pattern to a controller method.
 */
package jazzyframework.routing;

public class Route {
    private final String method;
    private final String path;
    private final String methodName;
    private final Class<?> controllerClass;

    /**
     * Creates a new Route with the specified attributes.
     * 
     * @param method The HTTP method (GET, POST, PUT, DELETE, PATCH)
     * @param path The URL path pattern (can include path parameters like {id})
     * @param methodName The name of the controller method to invoke
     * @param controllerClass The controller class containing the method
     */
    public Route(String method, String path, String methodName, Class<?> controllerClass) {
        this.method = method;
        this.path = path;
        this.methodName = methodName;
        this.controllerClass = controllerClass;
    }

    /**
     * @return The HTTP method of this route
     */
    public String getMethod() { return method; }
    
    /**
     * @return The URL path pattern of this route
     */
    public String getPath() { return path; }
    
    /**
     * @return The name of the controller method to invoke
     */
    public String getMethodName() { return methodName; }
    
    /**
     * @return The controller class containing the method
     */
    public Class<?> getControllerClass() { return controllerClass; }
}
