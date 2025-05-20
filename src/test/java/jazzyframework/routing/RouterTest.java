package jazzyframework.routing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Tests for the Router class.
 */
public class RouterTest {
    
    /**
     * Test for adding a GET route and finding it.
     */
    @Test
    public void testAddAndFindGetRoute() {
        // Given
        Router router = new Router();
        router.GET("/test", "testMethod", TestController.class);
        
        // When
        Route route = router.findRoute("GET", "/test");
        
        // Then
        assertNotNull(route, "Route should be found");
        assertEquals("GET", route.getMethod(), "HTTP method should match");
        assertEquals("/test", route.getPath(), "Path should match");
        assertEquals("testMethod", route.getMethodName(), "Method name should match");
        assertEquals(TestController.class, route.getControllerClass(), "Controller class should match");
    }
    
    /**
     * Test for adding routes with different HTTP methods.
     */
    @Test
    public void testMultipleHttpMethods() {
        // Given
        Router router = new Router();
        router.GET("/resource", "getResource", TestController.class);
        router.POST("/resource", "createResource", TestController.class);
        router.PUT("/resource", "updateResource", TestController.class);
        router.DELETE("/resource", "deleteResource", TestController.class);
        
        // When & Then
        assertNotNull(router.findRoute("GET", "/resource"), "GET route should be found");
        assertNotNull(router.findRoute("POST", "/resource"), "POST route should be found");
        assertNotNull(router.findRoute("PUT", "/resource"), "PUT route should be found");
        assertNotNull(router.findRoute("DELETE", "/resource"), "DELETE route should be found");
    }
    
    /**
     * Test for finding routes with path parameters.
     */
    @Test
    public void testFindRouteWithPathParameters() {
        // Given
        Router router = new Router();
        router.GET("/users/{id}", "getUserById", TestController.class);
        
        // When
        Route route = router.findRoute("GET", "/users/123");
        
        // Then
        assertNotNull(route, "Route with path parameters should be found");
        assertEquals("/users/{id}", route.getPath(), "Path pattern should match");
    }
    
    /**
     * Test for extracting path parameters from a URL.
     */
    @Test
    public void testExtractPathParameters() {
        // Given
        Router router = new Router();
        String routePath = "/users/{id}/posts/{postId}";
        String actualPath = "/users/123/posts/456";
        
        // When
        Map<String, String> params = router.extractPathParams(routePath, actualPath);
        
        // Then
        assertEquals(2, params.size(), "Should extract 2 parameters");
        assertEquals("123", params.get("id"), "User ID parameter should match");
        assertEquals("456", params.get("postId"), "Post ID parameter should match");
    }
    
    /**
     * Test for supported HTTP methods.
     */
    @Test
    public void testIsSupportedMethod() {
        // Given
        Router router = new Router();
        
        // Then
        assertTrue(router.isSupportedMethod("GET"), "GET should be supported");
        assertTrue(router.isSupportedMethod("POST"), "POST should be supported");
        assertTrue(router.isSupportedMethod("PUT"), "PUT should be supported");
        assertTrue(router.isSupportedMethod("DELETE"), "DELETE should be supported");
        assertTrue(router.isSupportedMethod("PATCH"), "PATCH should be supported");
        assertFalse(router.isSupportedMethod("HEAD"), "HEAD should not be supported");
        assertFalse(router.isSupportedMethod("OPTIONS"), "OPTIONS should not be supported");
    }
    
    /**
     * Test for route not found.
     */
    @Test
    public void testRouteNotFound() {
        // Given
        Router router = new Router();
        
        // When
        Route route = router.findRoute("GET", "/nonexistent");
        
        // Then
        assertNull(route, "Route should not be found");
    }
    
    /**
     * Mock controller class for testing.
     */
    private static class TestController {
        public String testMethod() {
            return "Test";
        }
        
        public String getResource() {
            return "Get Resource";
        }
        
        public String createResource() {
            return "Create Resource";
        }
        
        public String updateResource() {
            return "Update Resource";
        }
        
        public String deleteResource() {
            return "Delete Resource";
        }
        
        public String getUserById(String id) {
            return "User " + id;
        }
    }
} 