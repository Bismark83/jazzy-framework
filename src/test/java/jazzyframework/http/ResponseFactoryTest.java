package jazzyframework.http;

import static org.junit.jupiter.api.Assertions.*;
import static jazzyframework.http.ResponseFactory.response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Tests for the ResponseFactory class.
 */
public class ResponseFactoryTest {
    
    /**
     * Test for creating JSON response from an object.
     */
    @Test
    public void testJsonWithObject() {
        // Given
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Trevor");
        data.put("age", 30);
        
        // When
        Response response = response().json(data);
        
        // Then
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getBody().contains("\"name\":\"Trevor\""));
        assertTrue(response.getBody().contains("\"age\":30"));
        assertEquals(200, response.getStatusCode());
    }
    
    /**
     * Test for creating JSON response from key-value pairs.
     */
    @Test
    public void testJsonWithKeyValues() {
        // When
        Response response = response().json(
            "name", "Cassandra",
            "age", 25,
            "roles", Arrays.asList("admin", "user")
        );
        
        // Then
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getBody().contains("\"name\":\"Cassandra\""));
        assertTrue(response.getBody().contains("\"age\":25"));
        assertTrue(response.getBody().contains("\"roles\":[\"admin\",\"user\"]"));
        assertEquals(200, response.getStatusCode());
    }
    
    /**
     * Test for creating JSON response with invalid key-value pairs (odd number).
     */
    @Test
    public void testJsonWithInvalidKeyValues() {
        // This should throw an IllegalArgumentException due to odd number of arguments
        assertThrows(IllegalArgumentException.class, () -> 
            response().json("name", "Marcus", "age")
        );
    }
    
    /**
     * Test for creating JSON response with invalid key-value pairs (non-string key).
     */
    @Test
    public void testJsonWithNonStringKeys() {
        // This should throw an IllegalArgumentException due to non-string key
        assertThrows(IllegalArgumentException.class, () -> 
            response().json(123, "Marcus")
        );
    }
    
    /**
     * Test for creating success response.
     */
    @Test
    public void testSuccess() {
        // When
        Response response = response().success("Operation completed");
        
        // Then
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getBody().contains("\"status\":\"success\""));
        assertTrue(response.getBody().contains("\"message\":\"Operation completed\""));
        assertEquals(200, response.getStatusCode());
    }
    
    /**
     * Test for creating success response with data.
     */
    @Test
    public void testSuccessWithData() {
        // Given
        Map<String, Object> user = new HashMap<>();
        user.put("id", "123");
        user.put("name", "Penelope");
        
        // When
        Response response = response().success("User created", user);
        
        // Then
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getBody().contains("\"status\":\"success\""));
        assertTrue(response.getBody().contains("\"message\":\"User created\""));
        assertTrue(response.getBody().contains("\"data\":{"));
        assertTrue(response.getBody().contains("\"id\":\"123\""));
        assertTrue(response.getBody().contains("\"name\":\"Penelope\""));
        assertEquals(200, response.getStatusCode());
    }
    
    /**
     * Test for creating error response.
     */
    @Test
    public void testError() {
        // When
        Response response = response().error("Not found");
        
        // Then
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getBody().contains("\"status\":\"error\""));
        assertTrue(response.getBody().contains("\"message\":\"Not found\""));
        assertEquals(400, response.getStatusCode());
    }
    
    /**
     * Test for creating error response with custom status code.
     */
    @Test
    public void testErrorWithStatusCode() {
        // When
        Response response = response().error("Forbidden", 403);
        
        // Then
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getBody().contains("\"status\":\"error\""));
        assertTrue(response.getBody().contains("\"message\":\"Forbidden\""));
        assertEquals(403, response.getStatusCode());
    }
    
    /**
     * Test for creating text response.
     */
    @Test
    public void testText() {
        // Given
        String text = "Plain text content";
        
        // When
        Response response = response().text(text);
        
        // Then
        assertEquals("text/plain", response.getContentType());
        assertEquals(text, response.getBody());
        assertEquals(200, response.getStatusCode());
    }
    
    /**
     * Test for creating HTML response.
     */
    @Test
    public void testHtml() {
        // Given
        String html = "<h1>Title</h1><p>Content</p>";
        
        // When
        Response response = response().html(html);
        
        // Then
        assertEquals("text/html", response.getContentType());
        assertEquals(html, response.getBody());
        assertEquals(200, response.getStatusCode());
    }
    
    /**
     * Test for creating redirect response.
     */
    @Test
    public void testRedirect() {
        // Given
        String url = "/dashboard";
        
        // When
        Response response = response().redirect(url);
        
        // Then
        assertEquals(302, response.getStatusCode());
        assertEquals(url, response.getHeaders().get("Location"));
    }
    
    /**
     * Test for method chaining with status.
     */
    @Test
    public void testMethodChainingWithStatus() {
        // When
        Response response = response().json("name", "Dexter").status(201);
        
        // Then
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getBody().contains("\"name\":\"Dexter\""));
        assertEquals(201, response.getStatusCode());
    }
    
    /**
     * Test for creating JSON response with a JSON builder object.
     */
    @Test
    public void testJsonWithJSONBuilder() {
        // Given
        JSON json = JSON.of(
            "name", "Victoria",
            "age", 29,
            "roles", JSON.array("admin", "editor", "user")
        );
        
        // When
        Response response = response().json(json);
        
        // Then
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getBody().contains("\"name\":\"Victoria\""));
        assertTrue(response.getBody().contains("\"age\":29"));
        assertTrue(response.getBody().contains("\"roles\":[\"admin\",\"editor\",\"user\"]"));
        assertEquals(200, response.getStatusCode());
    }
    
    /**
     * Test for creating success response with JSON data.
     */
    @Test
    public void testSuccessWithJSONData() {
        // Given
        JSON user = JSON.of(
            "id", "456",
            "name", "Alexander"
        );
        
        // When
        Response response = response().success("User created", user);
        
        // Then
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getBody().contains("\"status\":\"success\""));
        assertTrue(response.getBody().contains("\"message\":\"User created\""));
        assertTrue(response.getBody().contains("\"data\":{"));
        assertTrue(response.getBody().contains("\"id\":\"456\""));
        assertTrue(response.getBody().contains("\"name\":\"Alexander\""));
        assertEquals(200, response.getStatusCode());
    }
} 