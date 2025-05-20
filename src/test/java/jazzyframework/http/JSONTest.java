package jazzyframework.http;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Tests for the JSON utility class.
 */
public class JSONTest {
    
    @Test
    public void testOf() {
        // When
        JSON json = JSON.of(
            "name", "Oliver",
            "age", 28,
            "city", "London"
        );
        
        // Then
        Map<String, Object> map = json.toMap();
        assertEquals(3, map.size());
        assertEquals("Oliver", map.get("name"));
        assertEquals(28, map.get("age"));
        assertEquals("London", map.get("city"));
    }
    
    @Test
    public void testOfWithOddArguments() {
        // This should throw an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> JSON.of("name", "Ryan", "age"));
    }
    
    @Test
    public void testOfWithNonStringKey() {
        // This should throw an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> JSON.of(123, "value"));
    }
    
    @Test
    public void testCreateAndAdd() {
        // When
        JSON json = JSON.create()
            .add("name", "Rebecca")
            .add("age", 31)
            .add("active", true);
        
        // Then
        Map<String, Object> map = json.toMap();
        assertEquals(3, map.size());
        assertEquals("Rebecca", map.get("name"));
        assertEquals(31, map.get("age"));
        assertEquals(true, map.get("active"));
    }
    
    @Test
    public void testArray() {
        // When
        List<Object> array = JSON.array("red", "green", "blue");
        
        // Then
        assertEquals(3, array.size());
        assertEquals("red", array.get(0));
        assertEquals("green", array.get(1));
        assertEquals("blue", array.get(2));
    }
    
    @Test
    public void testNestedObjects() {
        // When
        JSON address = JSON.of(
            "street", "123 Main St",
            "city", "Springfield"
        );
        
        JSON user = JSON.of(
            "name", "Diana",
            "address", address
        );
        
        // Then
        Map<String, Object> map = user.toMap();
        assertEquals(2, map.size());
        assertEquals("Diana", map.get("name"));
        
        Object addressObj = map.get("address");
        assertTrue(addressObj instanceof JSON);
        Map<String, Object> addressMap = ((JSON) addressObj).toMap();
        assertEquals("123 Main St", addressMap.get("street"));
        assertEquals("Springfield", addressMap.get("city"));
    }
    
    @Test
    public void testToString() {
        // When
        JSON json = JSON.of("name", "Maxwell");
        
        // Then
        String str = json.toString();
        assertTrue(str.contains("name"));
        assertTrue(str.contains("Maxwell"));
    }
} 