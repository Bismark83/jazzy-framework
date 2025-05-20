/**
 * A utility class to build JSON objects easily with a fluent API.
 * Allows creating JSON without having to use HashMap directly.
 */
package jazzyframework.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSON {
    private final Map<String, Object> data;
    
    /**
     * Creates a new empty JSON object.
     */
    private JSON() {
        this.data = new HashMap<>();
    }
    
    /**
     * Creates a new JSON builder.
     * 
     * @return A new JSON builder
     */
    public static JSON create() {
        return new JSON();
    }
    
    /**
     * Adds a key-value pair to the JSON object.
     * 
     * @param key The key
     * @param value The value
     * @return This JSON builder for method chaining
     */
    public JSON add(String key, Object value) {
        data.put(key, value);
        return this;
    }
    
    /**
     * Creates a new JSON object from key-value pairs.
     * 
     * @param keyValues The key-value pairs (must be even number of arguments)
     * @return A new JSON builder with the specified key-value pairs
     * @throws IllegalArgumentException if an odd number of arguments is provided
     */
    public static JSON of(Object... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("Must provide an even number of arguments");
        }
        
        JSON json = new JSON();
        for (int i = 0; i < keyValues.length; i += 2) {
            if (!(keyValues[i] instanceof String)) {
                throw new IllegalArgumentException("Keys must be strings");
            }
            json.data.put((String) keyValues[i], keyValues[i + 1]);
        }
        
        return json;
    }
    
    /**
     * Creates an array from the provided values.
     * 
     * @param values The array values
     * @return A List containing the values
     */
    public static List<Object> array(Object... values) {
        List<Object> array = new ArrayList<>();
        for (Object value : values) {
            array.add(value);
        }
        return array;
    }
    
    /**
     * Gets the data as a Map.
     * 
     * @return The JSON data as a Map
     */
    public Map<String, Object> toMap() {
        return data;
    }
    
    /**
     * String representation of the JSON object.
     */
    @Override
    public String toString() {
        return data.toString();
    }
} 