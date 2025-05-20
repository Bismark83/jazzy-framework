/**
 * Abstract base class for defining reusable validation rules.
 * Allows defining validation rules in a declarative way.
 */
package jazzyframework.http.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public abstract class ValidationRules {
    private final Map<String, ValidatorField> fields;
    private Validator validator;
    
    /**
     * Creates a new ValidationRules instance.
     */
    public ValidationRules() {
        this.fields = new HashMap<>();
    }
    
    /**
     * Sets the validator to use for validation.
     * This is called internally by the framework.
     * 
     * @param validator The validator to use
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    
    /**
     * Begins validation for a field.
     * 
     * @param fieldName The field name
     * @return A field validator
     */
    protected ValidatorField field(String fieldName) {
        if (validator == null) {
            throw new IllegalStateException("Validator not set. The ValidationRules must be used with Request.validate()");
        }
        
        ValidatorField field = validator.field(fieldName);
        fields.put(fieldName, field);
        return field;
    }
    
    /**
     * Creates a conditional validation rule.
     * The validation rules inside the condition will only be applied if the condition is true.
     * 
     * @param condition The condition to check
     * @param rules The validation rules to apply if the condition is true
     */
    protected void when(Predicate<Map<String, Object>> condition, Runnable rules) {
        if (validator == null) {
            throw new IllegalStateException("Validator not set. The ValidationRules must be used with Request.validate()");
        }
        
        // Get the data from the validator
        Map<String, Object> data = validator.getAllData();
        
        // Apply the rules if the condition is true
        if (condition.test(data)) {
            rules.run();
        }
    }
    
    /**
     * Applies validation rules only if a field has a value.
     * 
     * @param fieldName The field name
     * @param rules The validation rules to apply if the field has a value
     */
    protected void whenPresent(String fieldName, Runnable rules) {
        when(data -> data.containsKey(fieldName) && data.get(fieldName) != null, rules);
    }
    
    /**
     * Applies validation rules only if multiple fields are present.
     * 
     * @param fieldNames The field names
     * @param rules The validation rules to apply if all fields are present
     */
    protected void whenAllPresent(String[] fieldNames, Runnable rules) {
        when(data -> {
            for (String fieldName : fieldNames) {
                if (!data.containsKey(fieldName) || data.get(fieldName) == null) {
                    return false;
                }
            }
            return true;
        }, rules);
    }
    
    /**
     * Applies validation rules if any of the fields are present.
     * 
     * @param fieldNames The field names
     * @param rules The validation rules to apply if any field is present
     */
    protected void whenAnyPresent(String[] fieldNames, Runnable rules) {
        when(data -> {
            for (String fieldName : fieldNames) {
                if (data.containsKey(fieldName) && data.get(fieldName) != null) {
                    return true;
                }
            }
            return false;
        }, rules);
    }
    
    /**
     * Validates the data against the defined rules.
     * 
     * @return The validation result
     */
    public ValidationResult validate() {
        if (validator == null) {
            throw new IllegalStateException("Validator not set. The ValidationRules must be used with Request.validate()");
        }
        
        return validator.validate();
    }
    
    /**
     * Creates a validator with the given data and applies the rules.
     * 
     * @param data The data to validate
     * @return The validation result
     */
    public ValidationResult validate(Map<String, ?> data) {
        Validator validator = new Validator(data);
        setValidator(validator);
        return validate();
    }
} 