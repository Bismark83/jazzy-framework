package jazzyframework.http;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import jazzyframework.http.validation.ValidationResult;
import jazzyframework.http.validation.Validator;

/**
 * Tests for the Request class.
 */
public class RequestTest {
    
    @Test
    public void testGetMethod() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        assertEquals("GET", request.getMethod());
    }
    
    @Test
    public void testGetPath() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        assertEquals("/users/123/view", request.getPath());
    }
    
    @Test
    public void testHeader() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        assertEquals("application/json", request.header("content-type"));
        assertEquals("JUnit-Test", request.header("user-agent"));
        assertNull(request.header("non-existent"));
    }
    
    @Test
    public void testHeaderWithDefault() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        assertEquals("application/json", request.header("content-type", "text/plain"));
        assertEquals("default-value", request.header("non-existent", "default-value"));
    }
    
    @Test
    public void testGetHeaders() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        Map<String, String> allHeaders = request.getHeaders();
        assertEquals(2, allHeaders.size());
        assertEquals("application/json", allHeaders.get("content-type"));
        assertEquals("JUnit-Test", allHeaders.get("user-agent"));
    }
    
    @Test
    public void testPath() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        assertEquals("123", request.path("id"));
        assertEquals("view", request.path("action"));
        assertNull(request.path("non-existent"));
    }
    
    @Test
    public void testGetPathParams() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        Map<String, String> params = request.getPathParams();
        assertEquals(2, params.size());
        assertEquals("123", params.get("id"));
        assertEquals("view", params.get("action"));
    }
    
    @Test
    public void testQuery() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        assertEquals("2", request.query("page"));
        assertEquals("10", request.query("limit"));
        assertEquals("name", request.query("sort"));
        assertEquals("true", request.query("active"));
        assertNull(request.query("non-existent"));
    }
    
    @Test
    public void testQueryWithDefault() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        assertEquals("2", request.query("page", "1"));
        assertEquals("default-value", request.query("non-existent", "default-value"));
    }
    
    @Test
    public void testQueryInt() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        assertEquals(2, request.queryInt("page", 1));
        assertEquals(10, request.queryInt("limit", 20));
        assertEquals(99, request.queryInt("non-existent", 99));
        assertEquals(50, request.queryInt("sort", 50)); // Not a number, should return default
    }
    
    @Test
    public void testQueryBoolean() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        assertTrue(request.queryBoolean("active", false));
        assertFalse(request.queryBoolean("non-existent", false));
    }
    
    @Test
    public void testGetQueryParams() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        Map<String, String> params = request.getQueryParams();
        assertEquals(4, params.size());
        assertEquals("2", params.get("page"));
        assertEquals("10", params.get("limit"));
        assertEquals("name", params.get("sort"));
        assertEquals("true", params.get("active"));
    }
    
    @Test
    public void testGetBody() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        assertEquals(body, request.getBody());
    }
    
    @Test
    public void testJson() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        Map<String, Object> jsonData = request.json();
        assertEquals(3, jsonData.size());
        assertEquals("Rupert Mitchell", jsonData.get("name"));
        assertEquals("rupert@example.com", jsonData.get("email"));
        assertEquals(30, ((Number) jsonData.get("age")).intValue());
    }
    
    @Test
    public void testParseJson() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        Map<String, Object> jsonData = request.parseJson();
        assertEquals(3, jsonData.size());
        assertEquals("Rupert Mitchell", jsonData.get("name"));
        assertEquals("rupert@example.com", jsonData.get("email"));
        assertEquals(30, ((Number) jsonData.get("age")).intValue());
    }
    
    @Test
    public void testJsonWithEmptyBody() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        Request emptyRequest = new Request("GET", "/test", headers, pathParams, queryParams, "");
        Map<String, Object> result = emptyRequest.json();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testParseJsonWithEmptyBody() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        Request emptyRequest = new Request("GET", "/test", headers, pathParams, queryParams, "");
        Map<String, Object> result = emptyRequest.parseJson();
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testValidator() {
        // Create fresh maps for each test
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        queryParams.put("limit", "10");
        queryParams.put("sort", "name");
        queryParams.put("active", "true");
        
        String body = "{\"name\":\"Rupert Mitchell\",\"email\":\"rupert@example.com\",\"age\":30}";
        
        Request request = new Request("GET", "/users/123/view", headers, pathParams, queryParams, body);
        Validator validator = request.validator();
        ValidationResult result = validator
            .field("page").required().numeric()
            .field("limit").required().numeric()
            .validate();
        
        assertTrue(result.isValid());
    }
    
    @Test
    public void testValidatorWithInvalidData() {
        // Create a request with invalid data
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("user-agent", "JUnit-Test");
        
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("id", "123");
        pathParams.put("action", "view");
        
        Map<String, String> invalidQueryParams = new HashMap<>();
        invalidQueryParams.put("page", "not-a-number");
        
        // Create a body with invalid email
        String invalidBody = "{\"name\":\"Test User\",\"email\":\"invalid-email\",\"age\":30}";
        
        Request invalidRequest = new Request("GET", "/test", headers, pathParams, invalidQueryParams, invalidBody);
        
        Validator validator = invalidRequest.validator();
        ValidationResult result = validator
            .field("page").numeric()
            .field("email").email()
            .validate();
        
        assertFalse(result.isValid());
        // There are actually two errors - one for page and one for email
        assertEquals(2, result.getAllErrors().size());
        assertNotNull(result.getFirstError("page"));
        assertNotNull(result.getFirstError("email"));
    }
} 