/**
 * Interface for validation rules.
 */
package jazzyframework.http.validation;

public interface ValidationRule {
    /**
     * Checks if the value is valid according to this rule.
     * 
     * @param value The value to validate
     * @return true if the value is valid, false otherwise
     */
    boolean isValid(Object value);
    
    /**
     * Gets the error message for this rule.
     * 
     * @return The error message
     */
    String getMessage();
} 