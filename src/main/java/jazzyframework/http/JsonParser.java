/**
 * Simple JSON parser that converts JSON strings to Java objects.
 */
package jazzyframework.http;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class JsonParser {
    
    /**
     * Parse a JSON string into a Java object of the specified class.
     * 
     * @param <T> The type to convert to
     * @param json The JSON string
     * @param clazz The class of the target object
     * @return An instance of the class with fields populated from JSON
     * @throws Exception If parsing fails
     */
    public static <T> T parse(String json, Class<T> clazz) throws Exception {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        
        // For primitive types or strings, handle directly
        if (clazz == String.class) {
            return clazz.cast(json.replaceAll("^\"|\"$", "")); // Remove quotes if present
        }
        
        if (clazz == Integer.class || clazz == int.class) {
            return clazz.cast(Integer.parseInt(json));
        }
        
        if (clazz == Boolean.class || clazz == boolean.class) {
            return clazz.cast(Boolean.parseBoolean(json));
        }
        
        if (clazz == Double.class || clazz == double.class) {
            return clazz.cast(Double.parseDouble(json));
        }
        
        // For Map type
        if (Map.class.isAssignableFrom(clazz)) {
            return (T) parseMap(json);
        }
        
        // For custom objects
        return parseObject(json, clazz);
    }
    
    /**
     * Parse a JSON string into a Map.
     * 
     * @param json The JSON string
     * @return A Map containing the JSON data
     * @throws Exception If parsing fails
     */
    public static Map<String, Object> parseMap(String json) throws Exception {
        Map<String, Object> result = new HashMap<>();
        
        // Remove curly braces
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1).trim();
        }
        
        if (json.isEmpty()) {
            return result;
        }
        
        // Simple JSON parsing - this is not production-ready but works for basic cases
        int pos = 0;
        while (pos < json.length()) {
            // Find key
            int keyStart = json.indexOf("\"", pos);
            if (keyStart == -1) break;
            
            int keyEnd = json.indexOf("\"", keyStart + 1);
            if (keyEnd == -1) break;
            
            String key = json.substring(keyStart + 1, keyEnd);
            
            // Find value
            int colonPos = json.indexOf(":", keyEnd);
            if (colonPos == -1) break;
            
            // Find end of value
            int commaPos = findNextComma(json, colonPos);
            if (commaPos == -1) {
                commaPos = json.length();
            }
            
            String valueStr = json.substring(colonPos + 1, commaPos).trim();
            Object value = parseValue(valueStr);
            
            result.put(key, value);
            pos = commaPos + 1;
        }
        
        return result;
    }
    
    /**
     * Parse a JSON string into a custom object.
     * 
     * @param <T> The type to convert to
     * @param json The JSON string
     * @param clazz The class of the target object
     * @return An instance of the class with fields populated from JSON
     * @throws Exception If parsing fails
     */
    public static <T> T parseObject(String json, Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        Map<String, Object> map = parseMap(json);
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                Field field = clazz.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                
                // Simple type conversion
                Object value = entry.getValue();
                if (value instanceof String && field.getType() != String.class) {
                    value = convertStringToType((String) value, field.getType());
                }
                
                field.set(instance, value);
            } catch (NoSuchFieldException e) {
                // Skip fields that don't exist in the class
            }
        }
        
        return instance;
    }
    
    /**
     * Parse a string value into the appropriate type.
     * 
     * @param valueStr The string value
     * @return The parsed value
     */
    private static Object parseValue(String valueStr) {
        valueStr = valueStr.trim();
        
        // null
        if (valueStr.equals("null")) {
            return null;
        }
        
        // boolean
        if (valueStr.equals("true")) {
            return Boolean.TRUE;
        }
        if (valueStr.equals("false")) {
            return Boolean.FALSE;
        }
        
        // number
        if (valueStr.matches("-?\\d+(\\.\\d+)?")) {
            if (valueStr.contains(".")) {
                return Double.parseDouble(valueStr);
            } else {
                return Integer.parseInt(valueStr);
            }
        }
        
        // string
        if (valueStr.startsWith("\"") && valueStr.endsWith("\"")) {
            return valueStr.substring(1, valueStr.length() - 1);
        }
        
        // Default fallback
        return valueStr;
    }
    
    /**
     * Convert a string to the specified type.
     * 
     * @param value The string value
     * @param targetType The target type
     * @return The converted value
     */
    private static Object convertStringToType(String value, Class<?> targetType) {
        if (targetType == Integer.class || targetType == int.class) {
            return Integer.parseInt(value);
        }
        if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (targetType == Double.class || targetType == double.class) {
            return Double.parseDouble(value);
        }
        return value;
    }
    
    /**
     * Find the next comma that is not inside quotes.
     * 
     * @param json The JSON string
     * @param start The starting position
     * @return The position of the next comma or -1 if not found
     */
    private static int findNextComma(String json, int start) {
        boolean inQuotes = false;
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                return i;
            }
        }
        return -1;
    }
} 