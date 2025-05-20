/**
 * Validator class for request validation.
 * Provides a fluent API for defining validation rules.
 */
package jazzyframework.http.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Validator {
    private final Map<String, Object> data;
    private final Map<String, List<ValidationRule>> rules;
    
    /**
     * Creates a new Validator for the specified data.
     * 
     * @param data The data to validate
     */
    public Validator(Map<String, ?> data) {
        this.data = new HashMap<>();
        this.rules = new HashMap<>();
        
        // Convert all values to String for consistent validation
        if (data != null) {
            for (Map.Entry<String, ?> entry : data.entrySet()) {
                this.data.put(entry.getKey(), entry.getValue());
            }
        }
    }
    
    /**
     * Begins validation for a field.
     * 
     * @param fieldName The field name
     * @return A field validator
     */
    public ValidatorField field(String fieldName) {
        return new ValidatorField(this, fieldName);
    }
    
    /**
     * Adds a validation rule for a field.
     * Used internally by ValidatorField.
     * 
     * @param fieldName The field name
     * @param rule The validation rule
     */
    void addRule(String fieldName, ValidationRule rule) {
        rules.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(rule);
    }
    
    /**
     * Gets the value of a field.
     * Used internally by validators that compare fields.
     * 
     * @param fieldName The field name
     * @return The field value
     */
    public Object getValue(String fieldName) {
        return data.get(fieldName);
    }
    
    /**
     * Gets all data being validated.
     * Used internally for conditional validation.
     * 
     * @return An unmodifiable map of all data
     */
    public Map<String, Object> getAllData() {
        return Collections.unmodifiableMap(data);
    }
    
    /**
     * Validates the data against the defined rules.
     * 
     * @return The validation result
     */
    public ValidationResult validate() {
        ValidationResult result = new ValidationResult();
        
        for (Map.Entry<String, List<ValidationRule>> entry : rules.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = data.get(fieldName);
            
            for (ValidationRule rule : entry.getValue()) {
                if (!rule.isValid(fieldValue)) {
                    result.addError(fieldName, rule.getMessage());
                    break; // Stop validating this field once an error is found
                }
            }
        }
        
        return result;
    }
} 