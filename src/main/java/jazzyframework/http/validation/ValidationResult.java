/**
 * Represents the result of a validation operation.
 */
package jazzyframework.http.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ValidationResult {
    private final Map<String, List<String>> errors;
    
    /**
     * Creates a new validation result.
     */
    public ValidationResult() {
        this.errors = new HashMap<>();
    }
    
    /**
     * Adds an error message for a field.
     * 
     * @param field The field name
     * @param message The error message
     */
    public void addError(String field, String message) {
        errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }
    
    /**
     * Checks if the validation passed (no errors).
     * 
     * @return true if validation passed, false otherwise
     */
    public boolean isValid() {
        return errors.isEmpty();
    }
    
    /**
     * Checks if the validation failed (has errors).
     * 
     * @return true if validation failed, false otherwise
     */
    public boolean failed() {
        return !isValid();
    }
    
    /**
     * Gets all error messages for a field.
     * 
     * @param field The field name
     * @return A list of error messages, or an empty list if no errors
     */
    public List<String> getErrors(String field) {
        return errors.getOrDefault(field, Collections.emptyList());
    }
    
    /**
     * Gets the first error message for a field.
     * 
     * @param field The field name
     * @return The first error message, or null if no errors
     */
    public String getFirstError(String field) {
        List<String> fieldErrors = errors.get(field);
        return fieldErrors != null && !fieldErrors.isEmpty() ? fieldErrors.get(0) : null;
    }
    
    /**
     * Gets all errors.
     * 
     * @return A map of field names to error messages
     */
    public Map<String, List<String>> getAllErrors() {
        return Collections.unmodifiableMap(errors);
    }
    
    /**
     * Gets a map with only the first error for each field.
     * 
     * @return A map of field names to first error messages
     */
    public Map<String, String> getFirstErrors() {
        Map<String, String> firstErrors = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : errors.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                firstErrors.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        return firstErrors;
    }
    
    /**
     * Checks if a specific field has validation errors.
     * 
     * @param field The field name
     * @return true if the field has errors, false otherwise
     */
    public boolean hasError(String field) {
        return errors.containsKey(field) && !errors.get(field).isEmpty();
    }
    
    /**
     * Gets the total number of validation errors.
     * 
     * @return The total number of errors
     */
    public int getErrorCount() {
        int count = 0;
        for (List<String> fieldErrors : errors.values()) {
            count += fieldErrors.size();
        }
        return count;
    }
    
    /**
     * Executes a callback if validation passed.
     * 
     * @param callback The callback to execute
     * @return This validation result for method chaining
     */
    public ValidationResult onSuccess(Runnable callback) {
        if (isValid()) {
            callback.run();
        }
        return this;
    }
    
    /**
     * Executes a callback if validation failed.
     * 
     * @param callback The callback to execute
     * @return This validation result for method chaining
     */
    public ValidationResult onFailure(Consumer<Map<String, List<String>>> callback) {
        if (!isValid()) {
            callback.accept(Collections.unmodifiableMap(errors));
        }
        return this;
    }
    
    /**
     * Returns a result from the validation.
     * 
     * @param <T> The type of the result
     * @param successCallback The callback to execute if validation passed
     * @param failureCallback The callback to execute if validation failed
     * @return The result from the appropriate callback
     */
    public <T> T fold(Function<Void, T> successCallback, Function<Map<String, List<String>>, T> failureCallback) {
        if (isValid()) {
            return successCallback.apply(null);
        } else {
            return failureCallback.apply(Collections.unmodifiableMap(errors));
        }
    }
    
    /**
     * Merges another validation result into this one.
     * 
     * @param other The other validation result
     * @return This validation result for method chaining
     */
    public ValidationResult merge(ValidationResult other) {
        for (Map.Entry<String, List<String>> entry : other.errors.entrySet()) {
            for (String message : entry.getValue()) {
                addError(entry.getKey(), message);
            }
        }
        return this;
    }
    
    /**
     * Performs an action and returns its result if validation passed.
     * A more straightforward alternative to fold() for controller methods.
     * 
     * @param <T> The type of the result
     * @param action The action to perform if validation passed
     * @return The result of the action if validation passed, null otherwise
     */
    public <T> T onValidationSuccess(Function<Void, T> action) {
        if (isValid()) {
            return action.apply(null);
        }
        return null;
    }
    
    /**
     * Performs an action and returns its result if validation failed.
     * A more straightforward alternative to fold() for controller methods.
     * 
     * @param <T> The type of the result
     * @param action The action to perform if validation failed, receiving all errors
     * @return The result of the action if validation failed, null otherwise
     */
    public <T> T onValidationFailure(Function<Map<String, List<String>>, T> action) {
        if (failed()) {
            return action.apply(Collections.unmodifiableMap(errors));
        }
        return null;
    }
} 