package jazzyframework.routing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests for the Route class.
 */
public class RouteTest {
    
    /**
     * Test for route constructor and getters.
     */
    @Test
    public void testRouteConstructorAndGetters() {
        // Given
        String method = "GET";
        String path = "/test";
        String methodName = "testMethod";
        Class<?> controllerClass = TestController.class;
        
        // When
        Route route = new Route(method, path, methodName, controllerClass);
        
        // Then
        assertEquals(method, route.getMethod(), "Method should match");
        assertEquals(path, route.getPath(), "Path should match");
        assertEquals(methodName, route.getMethodName(), "Method name should match");
        assertEquals(controllerClass, route.getControllerClass(), "Controller class should match");
    }
    
    /**
     * Test for route with path parameters.
     */
    @Test
    public void testRouteWithPathParameters() {
        // Given
        String path = "/users/{id}";
        
        // When
        Route route = new Route("GET", path, "getUser", TestController.class);
        
        // Then
        assertEquals(path, route.getPath(), "Path with parameters should match");
    }
    
    /**
     * Mock controller class for testing.
     */
    private static class TestController {
        public String testMethod() {
            return "Test";
        }
        
        public String getUser(String id) {
            return "User " + id;
        }
    }
} 